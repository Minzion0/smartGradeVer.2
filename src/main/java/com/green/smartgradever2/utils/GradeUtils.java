package com.green.smartgradever2.utils;

public class GradeUtils {
    public int totalScore;
    public double rating;
    private String result = "";


    public GradeUtils(int totalScore) {
        this.totalScore = totalScore;
    }

    public GradeUtils() {

    }

    public String totalGradeFromScore1(int score) {
        double rating = totalScore2(score);
        return totalStrRating(rating);
    }

    public String totalGradeFromScore(int score) {
        double rating = totalScore2(score);
        return String.format("%.1f", rating);
    }

    public double setDouRating(double avg, int index) {
        return Math.round(avg / index * 10) / 10.0;
    }

    public String totalStrRating(double rating) {
        if (rating == 4.5) {
            this.result = "A+";
        } else if (rating == 4.0) {
            this.result = "A";
        } else if (rating == 3.5) {
            this.result = "B+";
        } else if (rating == 3.0) {
            this.result = "B";
        } else if (rating == 2.5) {
            this.result = "C+";
        } else if (rating == 2.0) {
            this.result = "C";
        } else if (rating == 1.5) {
            this.result = "D+";
        } else if (rating == 1.0) {
            this.result = "D";
        } else {
            this.result = "F";
        }
        return this.result;
    }

    public double totalScore() {
        if (totalScore >= 90) {
            rating = 4.0;
            if (totalScore > 94) {
                rating = 4.5;
            }
        } else if (totalScore >= 80) {
            rating = 3.0;
            if (totalScore > 84) {
                rating = 3.5;
            }
        } else if (totalScore >= 70) {
            rating = 2.0;
            if (totalScore > 74) {
                rating = 2.5;
            }
        } else if (totalScore >= 60) {
            rating = 1.0;
            if (totalScore >= 64) {
                rating = 1.5;
            }
        } else {
            rating = 0;
        }

        return rating;
    }

    public double totalScore2(int score) {
        if (score >= 90) {
            this.rating = 4.0;
            if (score > 94) {
                this.rating = 4.5;
            }
        } else if (score >= 80) {
            this.rating = 3.0;
            if (score > 84) {
                this.rating = 3.5;
            }
        } else if (score >= 70) {
            this.rating = 2.0;
            if (score > 74) {
                this.rating = 2.5;
            }
        } else if (score >= 60) {
            this.rating = 1.0;
            if (score >= 64) {
                this.rating = 1.5;
            }
        } else {
            this.rating = 0;
        }
        return this.rating;
    }

}
//class GradeUtils2{
//    private  double pp;
//    private  String grade;
//
//    public double getPp() {
//        return pp;
//    }
//
//    public String getGrade() {
//        return grade;
//    }
//
//    public void getGrade(int totalScore){
//        int temp= totalScore/10;
//        int temp2=totalScore%10;
//        double result=0;
//        String grade="F";
//        if (temp>=9){
//            result=4.0;
//            grade="A";
//        }else if (temp>=8){
//            result= 3.0;
//            grade="B";
//        }else if (temp>=7){
//            result=2.0;
//            grade="C";
//        } else if (temp >= 6) {
//            result=1.0;
//            grade="D";
//
//        }
//
//        if (temp2>4 || totalScore== 100){
//            result+=0.5;
//            grade+="+";
//        }
//        this.pp=result;
//        this.grade=grade;
//    }
//
//
//}