package test;

import java.util.Collection;
import datibernate.ReturnType;
import datibernate.Attribute;

public interface User2 {
    @Attribute(value="name")
    String getName();
@Attribute(value="address")
    @ReturnType(value=Address.class)
    Address getAddress();
@Attribute(value="nickname")
    Collection<String> getNicknames();
@Attribute(value="toy")
    @ReturnType(value=Toy.class)
    Collection<Toy> getToy();
@Attribute(value="gender")
    @ReturnType(value=Gender.class)
    Gender getGender();

    enum Gender {MALE,FEMALE}
    
}
