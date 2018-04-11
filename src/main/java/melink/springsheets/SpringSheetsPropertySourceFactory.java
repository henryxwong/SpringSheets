package melink.springsheets;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class SpringSheetsPropertySourceFactory implements PropertySourceFactory {

    private static Sheets.Spreadsheets spreadsheets;

    static {
        try {
            spreadsheets = SpringSheets.getSheetsService().spreadsheets();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        String[] pathName = resource.getResource().getFilename().split(",");
        String spreadsheetId = pathName[0];
        String range = pathName[1];
        String valueRenderOption = "UNFORMATTED_VALUE";
        String fields = "values";

        ValueRange valueRange = spreadsheets.values().get(spreadsheetId, range).setValueRenderOption(valueRenderOption).setFields(fields).execute();
        List<List<Object>> values = valueRange.getValues();
        HashMap<String, Object> map = new HashMap<>();

        boolean tableMode = false;
        List<Object> firstRow = null;
        for (List<Object> row : values) {
            if (firstRow == null) {
                firstRow = row;
                if (firstRow.get(0).toString().isEmpty()) {
                    tableMode = true;
                    continue;
                }
            }

            if (tableMode) {
                for (int i = 1; i < firstRow.size(); i++) {
                    map.put(row.get(0) + "." + firstRow.get(i), row.get(i));
                }
            } else {
                for (int i = 1; i < firstRow.size(); i++) {
                    map.put(row.get(0).toString(), row.get(i));
                }
            }
        }

        return new MapPropertySource(name, map);
    }
}
