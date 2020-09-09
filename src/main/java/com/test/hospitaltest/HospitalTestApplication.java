package com.test.hospitaltest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.hospitaltest.entity.Patient;

import com.test.hospitaltest.entity.PatientWithScore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.sql.SQLOutput;
import java.util.*;


@SpringBootApplication
public class HospitalTestApplication {

    public static   Set<PatientWithScore> averageBehaviorData(List<Patient> patinetList, String longitude, String latitude){
        Set<PatientWithScore> selectedPatients= new HashSet<>();
        List<PatientWithScore> littleBehaviorPatients=new ArrayList<>();
        List<PatientWithScore> patientsWithScores=new ArrayList<>();
        ArrayList <Integer> totalNumberAcceptedOffers=new ArrayList<>();
        ArrayList <Integer> totalCancelledOffers=new ArrayList<>();
        ArrayList <Integer> totalReplyTime=new ArrayList<>();

        for (Patient patient:patinetList) {
            totalNumberAcceptedOffers.add( patient.getAcceptedOffers());
            totalCancelledOffers.add(patient.getCanceledOffers());
            totalReplyTime.add(patient.getAverageReplyTime());
        }
        int maxAcceptedOffers= Collections.max(totalNumberAcceptedOffers);
        int maxCancelledOffers=Collections.max(totalCancelledOffers);
        int maxReplyTime=Collections.max(totalReplyTime);


        for (Patient patient: patinetList) {
            PatientWithScore patientWithScore=new PatientWithScore();
            patientWithScore.setPatient(patient);
              int totalShareInMaxAccepted=(patient.getAcceptedOffers()*100)/maxAcceptedOffers;
              int scorePositive=0;
              for (float i = 0.1f; scorePositive <=10 ; i=i+0.1f) {
                    if(totalShareInMaxAccepted<=maxAcceptedOffers*i){
                        int totalShareInMaxCancelled=(patient.getCanceledOffers()*100)/maxCancelledOffers;
                        int scoreNegative=10;
                        for (float j = 0.1f; scoreNegative >=1 ; j=j+0.1f) {
                            if(totalShareInMaxCancelled<=maxCancelledOffers*j){
                                patientWithScore.setScore((scorePositive+scoreNegative)/2);
                                scorePositive=0;
                                scoreNegative=10;
                            break;

                            }
                            scoreNegative--;
                        }
                    break;
                    }else {
                    scorePositive++;
                    }
                }
            patientsWithScores.add(patientWithScore);
        }
        for (int i=0;i<patientsWithScores.size();i++) {
            if(patientsWithScores.get(i).getPatient().getAcceptedOffers()<=maxAcceptedOffers/2
                    && patientsWithScores.get(i).getPatient().getCanceledOffers()>=maxCancelledOffers/2
                    && patientsWithScores.get(i).getPatient().getAverageReplyTime()>=maxReplyTime/2
            ){
                littleBehaviorPatients.add(patientsWithScores.get(i));
                patientsWithScores.remove(i);
            }
        }

        int random=(int)Math.round(Math.random()*10);

            int limit=0;
            while (limit<10) {
                if(random>=5){
                    for (int j = 0; j <random ; j++) {
                        if(Float.toString(littleBehaviorPatients.get(j).getPatient().getLocation().getLatitude()).startsWith(latitude)
                        || Float.toString(littleBehaviorPatients.get(j).getPatient().getLocation().getLongitude()).startsWith(longitude)
                        )
                        {
                            selectedPatients.add(littleBehaviorPatients.get(j));
                            limit++;

                        }
                    }
                } if(limit!=10){
                    for (int j = 0; j <patientsWithScores.size() ; j++) {
                        if(Float.toString(patientsWithScores.get(j).getPatient().getLocation().getLatitude()).startsWith(latitude)
                                || Float.toString(patientsWithScores.get(j).getPatient().getLocation().getLongitude()).startsWith(longitude)
                        ){
                                selectedPatients.add(patientsWithScores.get(j));
                            limit++;
                            if(limit==10){
                                break;
                            }
                        }
                    }
                    }
                limit=10;
                }

        return  selectedPatients;
    }

    private static void Menu(){
        System.out.println("=======================================\n");
        System.out.println("Please choose operation");
        System.out.println("1 do test ");
        System.out.println("2 Documentation ");
        System.out.println("3 exit \n");
    }


    public static void main(String[] args) {
        SpringApplication.run(HospitalTestApplication.class, args);

        ObjectMapper mapper= new ObjectMapper();
        try {
            InputStream inputStream= new ClassPathResource("/static/json/patients.json").getInputStream();
        TypeReference<List<Patient>> typeReference= new TypeReference<List<Patient>> (){};
        List<Patient> patients= mapper.readValue(inputStream,typeReference);


            int exit=0;
            while (exit!=1){
                Menu();
                Scanner operation=new Scanner(System.in);
                switch (operation.nextInt()){
                    case 1:{
                        System.out.println("Welcome to my demo test task for \"ООО Global Solutions\"\n");
                        System.out.println("=====================================================\n");
                        System.out.println("!!! Please attention:  in Json data  name of city is not given instead longitude and latitude is given \n" +
                                "so when you enter longitude and latitude , program find people whose longitude or latitude begin with location that you entered . Thank you\n");
                        System.out.println("=====================================================\n");
                        Scanner scanner = new Scanner(System.in);


                        System.out.println("Please enter latitude\n");
                        String latitude=scanner.nextLine();
                        System.out.println("Please enter longitude\n");
                        String longitude=scanner.nextLine();

                        System.out.println(latitude+"   "+longitude);
                        Set<PatientWithScore> patientWithScores = averageBehaviorData(patients,longitude,latitude);
                        int i=1;
                        if(patientWithScores.size()>0){
                        for (PatientWithScore p:patientWithScores) {
                            System.out.println("========================================\n");
                            System.out.println(+i+" Client: " + p.getPatient().getName()+"  Client score "+p.getScore());
                            i++;
                        }
                        }else {
                            System.out.println("Clients are not found in this location");
                        }
                    }
                    break;
                    case 2:{
                        System.out.println(" Tasks 1\n" +
                                "computing score for each patient--->   For this I get 2 factors under the consideration number of accepted offers and number of cancelledOffers of each person" +
                                "\nI find max of number of accepted offers and max of number of cancelledOffers among all patients then I found share of single patient in max in both category then I gave score by both category" +
                                "\nthen I found average of to scores (patients scored direct proportionally by accepted offers and reverse proportionally by cancelledOffers) For example if patient's share of accepted offers from 90% to 100% (compared to max) he is given 10 score by the category of accepted offers" +
                                "\nbut his share of cancelledOffers is also from 90% to 100% he is given 1 score then both added and divided to 2 so score of that patient will be 9(from accepted offers and )+1 (from cancelledOffers  category)= 5 (final score)  \n" +
                                "Task2\n " +
                                "grouping patients to little behavior group . I generated little behavior group from patients whose number of accepted offers < max/2(average), " +
                                "\nnumber of cancelledOffers>max/2(average) and reply time>max/2(average)\n" +
                                "Task3\n" +
                                "generating list of patients by their location and little behavior patients added to list  randomly "
                        );
                    }
                    break;
                    case 3:{
                        exit=1;
                    }
                    break;
                    default:{
                        System.out.println("Unknown operation");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
