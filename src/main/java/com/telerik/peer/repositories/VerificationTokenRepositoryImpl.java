package com.telerik.peer.repositories;

import com.telerik.peer.exceptions.EntityNotFoundException;
import com.telerik.peer.models.VerificationToken;
import com.telerik.peer.repositories.contracts.VerificationTokenRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class VerificationTokenRepositoryImpl extends AbstractCRUDRepository<VerificationToken> implements VerificationTokenRepository {
    @Autowired
    public VerificationTokenRepositoryImpl(SessionFactory sessionFactory) {
        super(VerificationToken.class, sessionFactory);
    }

    @Override
    public VerificationToken findByTokenValue(String value) {
        return executeQueryFindOne("from VerificationToken where name = :name", Map.of("name", value))
                .orElseThrow(() -> new EntityNotFoundException("token", "value", value));
    }
}
