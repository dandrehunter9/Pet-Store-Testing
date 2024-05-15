package com.petstore;


import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.petstoreservices.exceptions.PetDataStoreException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 * Class to support reading the json file
 */
public class PetStoreReader
{
    public PetStoreReader()
    {

    }

    /**
     * Read the json file and convert the JsonArray into a list of pet entity objects.
     * This could be a datastore but for teaching purposes easier to implement using a file for now
     * @return - the converted Json Objects
     * @throws PetDataStoreException - Issue with file format, reading the file, or file is not present
     */
    public List<PetEntity> readJsonFromFile() throws PetDataStoreException {
        List<PetEntity> entityList = new ArrayList<PetEntity>();
        try {

            InputStream stream = new FileInputStream("./datastore/application/petstore.json");
            JsonReader reader = new JsonReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            Gson gson = new Gson();
            // Read file in stream mode
            reader.beginArray();
            while (reader.hasNext()) {
                PetEntity carInfo = gson.fromJson(reader, PetEntity.class);
                entityList.add(carInfo);
            }
            reader.endArray();
            reader.close();
        } catch (UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
            throw new PetDataStoreException("Issue with the data store format!");
        } catch(FileNotFoundException ex)
        {
            ex.printStackTrace();
            throw new PetDataStoreException("Data store file at [./datastore/application/petstore.json] Not present!");
        }catch (IOException ex) {
            ex.printStackTrace();
            throw new PetDataStoreException("Issue reading the file");
        }
        return entityList;
    }


}
