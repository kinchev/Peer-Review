package com.telerik.peer.repositories.contracts;

import com.telerik.peer.models.VerificationToken;

public interface VerificationTokenRepository extends CRUDRepository<VerificationToken>{
    VerificationToken findByTokenValue(String value);
}
