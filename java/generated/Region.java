package generated;
import datomic.db.*;
import java.util.Collection;
import datibernate.ReturnType;
import datibernate.Attribute;
public interface Region{
@Attribute(value="region/s")
Collection getS();
@Attribute(value="region/sw")
Collection getSw();
@Attribute(value="region/w")
Collection getW();
@Attribute(value="region/nw")
Collection getNw();
@Attribute(value="region/n")
Collection getN();
@Attribute(value="region/ne")
Collection getNe();
@Attribute(value="region/e")
Collection getE();
@Attribute(value="region/se")
Collection getSe();

}