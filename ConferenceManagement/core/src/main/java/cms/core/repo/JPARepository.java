package cms.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface JPARepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
}
