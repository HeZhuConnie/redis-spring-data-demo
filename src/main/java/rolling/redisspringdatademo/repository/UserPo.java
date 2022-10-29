package rolling.redisspringdatademo.repository;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import rolling.redisspringdatademo.service.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "t_user")
public class UserPo implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    private String name;

    private String phone;

    private String password;

    private int age;

    public User toDomain() {
        User domain = new User();
        domain.setName(name);
        domain.setAge(age);
        domain.setPhone(phone);
        return domain;
    }
}
