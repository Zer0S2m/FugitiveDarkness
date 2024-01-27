package com.zer0s2m.fugitivedarkness.api.scheme;

import io.vertx.json.schema.common.dsl.ObjectSchemaBuilder;

import static io.vertx.json.schema.common.dsl.Schemas.*;
import static io.vertx.json.schema.common.dsl.Schemas.intSchema;

public class SchemaFilterSearch {

    public static final ObjectSchemaBuilder SCHEMA_FILTER_SEARCH = objectSchema()
            .requiredProperty("pattern", stringSchema())
            .requiredProperty("filters", objectSchema()
                    .requiredProperty("git", arraySchema()
                            .items(objectSchema()
                                    .requiredProperty("group", stringSchema())
                                    .requiredProperty("project", stringSchema()))
                    )
                    .optionalProperty("includeExtensionFiles", arraySchema()
                            .items(stringSchema())
                            .nullable())
                    .optionalProperty("excludeExtensionFiles", arraySchema()
                            .items(stringSchema())
                            .nullable())
                    .optionalProperty("patternForIncludeFile", stringSchema())
                    .optionalProperty("patternForExcludeFile", stringSchema())
                    .optionalProperty("maxCount", intSchema())
                    .optionalProperty("maxDepth", intSchema())
                    .optionalProperty("context", intSchema())
                    .optionalProperty("contextBefore", intSchema())
                    .optionalProperty("contextAfter", intSchema()));

}
