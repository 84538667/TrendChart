package com.example.trendchart;

import java.io.Serializable;

public class AllInf implements Serializable {
	
	//总gpa
	private String gpa;
	//各学期加权
	private float[] score ;
	//分数信息
	private ScoreInf[] sInf ;
	//学期
	private String[] term = new String[3];
	//总学分
	private float totalScore;
	
	public AllInf( String _gpa, float[] _score, ScoreInf[] _sInf, float _totalScore, String[] _term){
		this.gpa = _gpa;
		this.score = _score;
		this.sInf = _sInf;
		this.totalScore = _totalScore;
		this.term = _term;
	}
	
	public String getGPA(){
		return gpa;
	}
	
	public float[] getScore(){
		return score;
	}
	
	public ScoreInf[] getScoreInf(){
		return sInf;
	}
	
	public String[] getTerm(){
		return term;
	}
	
	public float getTotalScore(){
		return totalScore;
	}
	
}
