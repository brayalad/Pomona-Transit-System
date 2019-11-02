package models;

import java.io.Serializable;
import java.util.Map;

public interface TableDataModel extends Cloneable, Serializable {

    Object getPrimaryKey();

}
