package com.eviro.assessment.grad001.NompiloMalinga.fileParser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountProfileEntityRepo extends JpaRepository<AccountProfileEntity,Long> {
    AccountProfileEntity findByNameAndSurname(String name, String surname);
}
