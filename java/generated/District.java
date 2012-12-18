package generated;
import datomic.db.*;
import java.util.Collection;
import datibernate.ReturnType;
import datibernate.Attribute;
public interface District{
@Attribute(value="district/region")
@ReturnType(value=Region.class)
Region getRegion();
@Attribute(value="district/name")
String getName();

}