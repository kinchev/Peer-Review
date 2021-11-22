package com.telerik.peer.repositories;

import com.telerik.peer.models.Image;
import com.telerik.peer.repositories.contracts.ImageRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ImageRepositoryImpl extends AbstractCRUDRepository<Image> implements ImageRepository {

    private final SessionFactory sessionFactory;

    public ImageRepositoryImpl(SessionFactory sessionFactory) {
        super(Image.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }
}
