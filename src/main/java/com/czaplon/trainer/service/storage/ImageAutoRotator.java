package com.czaplon.trainer.service.storage;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;

public class ImageAutoRotator {

    private static Logger logger = LoggerFactory.getLogger(ImageAutoRotator.class);

    public static BufferedImage rotateImage(File file) throws ImageAutoRotatorException {
        try {
            BufferedImage image = ImageIO.read(file);

            Metadata metadata = ImageMetadataReader.readMetadata(file);
            ExifIFD0Directory exifIFD0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

            // w przypadku obrazka bez EXIFa
            if (exifIFD0==null) return image;

            int orientation = exifIFD0.getInt(ExifIFD0Directory.TAG_ORIENTATION);

            // w przypadku braku info o orientacji
            if (orientation==0) return image;

            logger.info("Read EXIF info from file - orientation: "+orientation);
//            Double rotation = 0.0;
            switch (orientation) {
                case 1: // [Exif IFD0] Orientation - Top, left side (Horizontal / normal)
//                    rotation = 0.0;  //Math.PI/1
                    return image;
//                    break;
                case 6: // [Exif IFD0] Orientation - Right side, top (Rotate 90 CW)
//                    rotation = Math.PI/2; //Math.PI/2
//                    break;
                    return rotate90(image,1);
                case 3: // [Exif IFD0] Orientation - Bottom, right side (Rotate 180)
//                    rotation = Math.PI; //Math.PI/1
//                    break;
                    return rotate90(image,2);
                case 8: // [Exif IFD0] Orientation - Left side, bottom (Rotate 270 CW)
//                    rotation = Math.PI*1.5;
//                    break;
                    return rotate90(image,3);
            }
//
//            AffineTransform affineTransform = new AffineTransform();
//            affineTransform.rotate(rotation,image.getWidth()/2,image.getHeight()/2);
//            double offset = (image.getWidth()-image.getHeight())/2;
//            affineTransform.translate(offset,offset);
//            AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform,AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
//
//            image = affineTransformOp.filter(image,null);

            return image;


        } catch (ImageProcessingException | MetadataException | IOException e) {
            throw new ImageAutoRotatorException(e.getMessage());
        }
//        return null;
    }

    public static BufferedImage rotateImage(MultipartFile file) throws  ImageAutoRotatorException {
        try {
            File fileToSaveTemp = File.createTempFile(file.getOriginalFilename(), "");
            file.transferTo(fileToSaveTemp);
            logger.info("Created temp file :" + fileToSaveTemp.getAbsolutePath());
            BufferedImage rotated = rotateImage(fileToSaveTemp);
            if (fileToSaveTemp.delete()) {
                logger.info("Deleted temp file");
            }
            return rotated;
        } catch (IOException e) {
            throw new ImageAutoRotatorException(e.getMessage());
        }

    }

    private static BufferedImage rotate90(BufferedImage source, int quadrants) {
        int width = source.getWidth();
        int height = source.getHeight();

        // Compute new width and height
        boolean swapXY = quadrants % 2 != 0;
        int newWidth = swapXY ? height : width;
        int newHeight = swapXY ? width : height;

        // Build transform
        AffineTransform transform = new AffineTransform();
        transform.translate(newWidth / 2.0, newHeight / 2.0);
        transform.quadrantRotate(quadrants);
        transform.translate(-width / 2.0, -height / 2.0);

        BufferedImageOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR); // 1:1 mapping
        logger.info("Image was rotated");
        return operation.filter(source, null);
    }
}
