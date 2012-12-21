package test;
import datomic.db.*;
import java.util.Collection;
import datibernate.ReturnType;
import datibernate.Attribute;
public interface MultiNamespace {

@Attribute(value="community/category")
Collection<String> getCategory();

@Attribute(value="neighborhood/name")
String getName();


}
