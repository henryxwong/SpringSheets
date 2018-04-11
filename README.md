# SpringSheets
Use Google Sheets as Spring property files

# Features
* Inject Google Sheets calculated cell values into your Spring applications directly
* Works with Spring @PropertySource and @Value annotations
* Works with Spring Boot @ConfigurationProperties for type-safe configuration properties (See https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config-typesafe-configuration-properties)
* Support both name-value pairs sheets or sheets with table structure

# Requirements
* Java 7
* Spring Framework 4+
* Google Account
* Works with Spring Boot (Optional)

# Maven
Not at the moment, I will put the lib into maven central if it got enough public interest. Please download the source and build it yourself at current stage.

# Usage
1. You should first follow this guide to turn on your Google Sheets API
https://developers.google.com/sheets/api/quickstart/java

2. Copy the generated client_secret.json file to your classpath

3. Annotate your Spring bean/config with @PropertySource with
    * name - a unique name for the property source
    * value - in this format <spreadsheetId,rangeName>
        * spreadsheetId is the google spreadsheet id
        * rangeName is the name of a named range within the sheet (See https://support.google.com/docs/answer/63175 if you have no idea how to create a named range)

For example:
```java
@Component
@PropertySources(
        {
                @PropertySource(name = "FeeTable", value = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX,TradingFee", factory = SpringSheetsPropertySourceFactory.class),
                @PropertySource(name = "ForexTable", value = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX,ForexRate", factory = SpringSheetsPropertySourceFactory.class)
        }
)
@ConfigurationProperties(prefix = "trading")
public class Config {
    ...
}

```

With a named range TradingFee in table style (headers required)

|                      | takerTradingFee | makerTradingFee |
|----------------------|-----------------|-----------------|
| trading.fee[BTC/USD] | 0.001           | 0.001           |
| trading.fee[ETH/USD] | 0.001           | 0.001           |
| trading.fee[ETH/BTC] | 0.001           | 0.001           |

and a named range ForexRate in name-value pairs style

|                         |      |
|-------------------------|------|
| trading.fxRate[USD/HKD] | 7.85 |
| trading.fxRate[EUR/HKD] | 9.75 |
