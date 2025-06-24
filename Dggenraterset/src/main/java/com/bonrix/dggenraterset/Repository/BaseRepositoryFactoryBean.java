package com.bonrix.dggenraterset.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
 
import javax.persistence.EntityManager;
import java.io.Serializable;
 
public class BaseRepositoryFactoryBean<R extends JpaRepository<T, I>, T,
        I extends Serializable> extends JpaRepositoryFactoryBean<R, T, I> {
 
    @SuppressWarnings("rawtypes")
	@Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
        return new BaseRepositoryFactory(em);
    }
 
    private static class BaseRepositoryFactory<T, I extends Serializable>
            extends JpaRepositoryFactory {
 
        private final EntityManager em;
 
        public BaseRepositoryFactory(EntityManager em) {
            super(em);
            this.em = em;
        }
 
        @SuppressWarnings("unchecked")
		@Override
        protected Object getTargetRepository(RepositoryMetadata metadata) {
            return new BaseRepositoryImpl<T, I>((Class<T>) metadata.getDomainType(), em);
        }
 
        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return BaseRepositoryImpl.class;
        }
    }
}
