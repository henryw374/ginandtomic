package generated;
import datomic.db.*;
import java.util.Collection;
import datibernate.ReturnType;
import datibernate.Attribute;
public interface Neighborhood{
@Attribute(value="neighborhood/district")
@ReturnType(value=District.class)
District getDistrict();
@Attribute(value="neighborhood/name")
String getName();

}