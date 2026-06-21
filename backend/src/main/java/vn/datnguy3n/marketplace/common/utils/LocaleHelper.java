package vn.datnguy3n.marketplace.common.utils;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LocaleHelper {

    public String getCurrentLanguage() {
        return LocaleContextHolder.getLocale().getLanguage();
    }
}
