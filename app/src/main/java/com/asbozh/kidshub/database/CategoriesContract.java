package com.asbozh.kidshub.database;

import android.provider.BaseColumns;

/**
 * Created by Nasko on 9/12/17.
 */

public class CategoriesContract {


    public static final class CategoriesEntry implements BaseColumns {


        public static final String CAT_TABLE_NAME = "category_table";

        public static final String COLUMN_CAT_ID = "cat_id";
        public static final String COLUMN_CAT_NAME = "cat_name";

        public static final String SUBCAT_TABLE_NAME = "subcategory_table";

        //public static final String COLUMN_CAT_ID = "cat_id";
        public static final String COLUMN_SUBCAT_ID = "subcat_id";
        public static final String COLUMN_SUBCAT_NAME = "subcat_name";
        public static final String COLUMN_COVER_URL = "cover_url";

        public static final String SUB_SUBCAT_TABLE_NAME = "sub_subcategory_table";

        //public static final String COLUMN_SUBCAT_ID = "subcat_id";
        public static final String COLUMN_SUB_SUBCAT_ID = "sub_subcat_id";
        public static final String COLUMN_SUB_SUBCAT_NAME = "sub_subcat_name";


    }
}
