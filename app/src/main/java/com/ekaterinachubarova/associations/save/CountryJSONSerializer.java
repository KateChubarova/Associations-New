package com.ekaterinachubarova.associations.save;

import android.content.Context;

import com.ekaterinachubarova.associations.banks.BankFifth;
import com.ekaterinachubarova.associations.banks.BankFirst;
import com.ekaterinachubarova.associations.banks.BankFourth;
import com.ekaterinachubarova.associations.banks.BankMainPictures;
import com.ekaterinachubarova.associations.banks.BankOfColors;
import com.ekaterinachubarova.associations.banks.BankOfCountries;
import com.ekaterinachubarova.associations.banks.BankSecond;
import com.ekaterinachubarova.associations.banks.BankSixth;
import com.ekaterinachubarova.associations.banks.BankThird;
import com.ekaterinachubarova.associations.essences.Country;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;

/**
 * Created by ekaterinachubarova on 25.08.16.
 */
public class CountryJSONSerializer {
    private Context context;
    private String fileName;

    public CountryJSONSerializer (Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public void saveCrimes (HashMap <Integer, Country> countries) throws JSONException, IOException{
        JSONArray countryArray = new JSONArray();
        for (int i=0; i<25; i++) {
            countryArray.put(countries.get(i).toJSON());
            System.out.println(countries.get(i).getEnglishName() + "i");
        }
        Writer writer = null;
        try {
            OutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(outputStream);
            writer.write(countryArray.toString());

            System.out.println("All is ok in saveCrimes in CountryJSONSer");

        } catch (FileNotFoundException e){
            System.out.println("Exception in countryJsonSer in method saveCrimes " + e);
        }
        finally {
            if (writer != null)
                writer.close();;
        }
    }

    public HashMap<Integer, Country> loadCountries () throws IOException, JSONException {
        HashMap<Integer, Country> countries = new HashMap<>();

        BufferedReader bufferedReader = null;

        try {
            //System.out.println(context);
            InputStream inputStream = context.openFileInput(fileName);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                jsonString.append(line);
            }

            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            for (int i=0; i<array.length(); i++){
                countries.put(i, new Country(array.getJSONObject(i)));
            }

        } catch (FileNotFoundException e) {

            countries = new HashMap<>();
            int count = 25;
            Country country;
            for (int i=0; i<count; i++) {
                country = new Country();
                country.setEnglishName(BankOfCountries.bank.get(i));
                country.setRussianName(BankOfCountries.bankR.get(i));

                country.setPicturesSmall(0, BankFirst.get(i));
                country.setPicturesSmall(1, BankSecond.get(i));
                country.setPicturesSmall(2, BankThird.get(i));
                country.setPicturesSmall(3, BankFourth.get(i));
                country.setPicturesSmall(4, BankFifth.get(i));
                country.setPicturesSmall(5, BankSixth.get(i));

                country.setMainPicture(BankMainPictures.get(i));
                country.setColor(BankOfColors.get(i));

                if (i==0) {
                    country.setIsOpen(Country.LEVEL_OPENED);
                }

                countries.put(i, country);
            }
            //writeFirst(context);
            System.out.println("Exception in countruJSONSer in method loadCount" + e);
        } finally {
            if (bufferedReader != null)
                bufferedReader.close();
        }

        return countries;
    }

}
