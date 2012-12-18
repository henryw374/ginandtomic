package generated;
import datomic.db.*;
import java.util.Collection;
import datibernate.ReturnType;
import datibernate.Attribute;
public interface Community{
@Attribute(value="community/name")
String getName();
@Attribute(value="community/neighborhood")
@ReturnType(value=Neighborhood.class)
Neighborhood getNeighborhood();
@Attribute(value="community/url")
String getUrl();
@Attribute(value="community/type")
@ReturnType(value=Type.class)
Type getType();
@Attribute(value="community/category")
Collection<String> getCategory();
@Attribute(value="community/orgtype")
@ReturnType(value=Orgtype.class)
Orgtype getOrgtype();
enum Orgtype {
commercial,
community,
personal,
nonprofit};
enum Type {
twitter,
emaillist,
blog,
facebookpage,
website,
wiki,
myspace,
ning};

}