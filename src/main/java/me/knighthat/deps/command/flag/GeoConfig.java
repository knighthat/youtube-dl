package me.knighthat.deps.command.flag;

import lombok.Builder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Builder
public class GeoConfig implements CommandFlag {

    @Builder.Default
    private boolean bypass     = false;
    @NotNull
    @Builder.Default
    private String  countyCode = Locale.getDefault().getCountry();

    @Override
    public @NotNull Map<String, String> arguments() {
        Map<String, String> results = new HashMap<>( 2 );

        if ( bypass )
            results.put( "--geo-bypass", "" );
        if ( !countyCode.isBlank() )
            results.put( "--geo-bypass-country", countyCode );

        return results;
    }
}
