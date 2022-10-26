package org.example.modal;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tour {


        private final List<Integer> nodes = new ArrayList();

        public Tour() {
        }

        public void load(BufferedReader reader) throws IOException {
            String line = null;

            while((line = reader.readLine()) != null) {
                String[] tokens = line.trim().split("\\s+");

                for(int i = 0; i < tokens.length; ++i) {
                    int id = Integer.parseInt(tokens[i]);
                    if (id == -1) {
                        break;
                    }

                    this.nodes.add(id);
                }
            }

        }

}
