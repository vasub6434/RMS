package com.bonrix.dggenraterset.Repository;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
 
import javax.persistence.EntityManager;
import java.io.Serializable;
 
public class BaseRepositoryImpl <T, ID extends Serializable>
			extends SimpleJpaRepository<T, ID>  implements BaseRepository<T, ID>{
         
    @SuppressWarnings("unused")
	private final EntityManager entityManager;
 
    public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }
 
}
