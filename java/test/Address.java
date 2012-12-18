package test;

import datibernate.Attribute;
public interface Address{
    public static final Class CLASS = Address.class;
@Attribute(value="zip")
    String getZip();
}
