/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import java.util.Scanner;

@SpringBootApplication
@EntityScan(basePackages = {"commons","server"})
public class Main {

    /**
     * Main method
     * @param args Main method arguments
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Main.class);
        app.run(args);
        System.out.println("RECEIVING THIS MESSAGE MEANS THAT THE SERVER HAS STARTED!");
        Scanner scanner = new Scanner(System.in);
        while(true) {
            String input = scanner.nextLine();
            switch(input.toLowerCase()){
                case "q":
                case "exit":
                    System.out.println("Stopping application...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Unknown command: "+ input);
                    break;
            }
        }

    }
}