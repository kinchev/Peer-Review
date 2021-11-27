package com.telerik.peer.services;

import com.telerik.peer.exceptions.UnauthorizedOperationException;
import com.telerik.peer.models.Image;
import com.telerik.peer.models.User;
import com.telerik.peer.repositories.contracts.ImageRepository;
import com.telerik.peer.services.contracts.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    private static final String MODIFY_IMAGE_ERROR_MESSAGE = "Only the user owner or admin can modify an user photo.";

    private final ImageRepository imageRepository;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public List<Image> getAll() {
        return imageRepository.getAll();
    }

    @Override
    public Image getById(long Id) {
        return imageRepository.getById(Id);
    }


    @Override
    public void update(Image image, User owner) {
        if (owner.getImage().getId() != image.getId()) {
            throw new UnauthorizedOperationException(MODIFY_IMAGE_ERROR_MESSAGE);
        }
        imageRepository.update(image);
    }

    @Override
    public void delete(long id, User owner) {
        if (owner.getImage().getId() != id) {
            throw new UnauthorizedOperationException(MODIFY_IMAGE_ERROR_MESSAGE);
        }
        imageRepository.delete(id);
    }



    @Override
    public void create(Image image) {
        imageRepository.create(image);

    }


}
