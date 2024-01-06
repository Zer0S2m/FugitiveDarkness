package com.zer0s2m.fugitivedarkness.provider;

import java.util.Map;

public record GitRepoProviderInfo(

        String host,

        String scheme,

        String path,

        Map<String, String> headers

) {
}
