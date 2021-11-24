package com.telerik.peer.services.contracts;

import com.telerik.peer.models.Image;
import com.telerik.peer.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    List<Image> getAll();

    Image getById(long Id);

    void update(Image image, User owner);

    void delete(long id, User owner);

    Long create(Image image);


}
