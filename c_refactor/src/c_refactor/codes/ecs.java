package c_refactor.codes;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

import c_refactor.swt.textCheckPage;
import c_refactor.swt.Rename_;


public class ecs {

   static String change; // x * y
   static String change_arr[] = new String[1000]; // change에 사용된 변수 모음
   static String change_name; // xy
   static int page = 0; // 검색하는 줄 수
   static int f, l = 0; // 검사 시작 줄 수,끝 줄 수
   static String result[] = new String[10000]; // 리팩토링 결과 저장 배열
   static int res_ind = 0; // result index
   static int change_page; // change 해당 줄 수
   static String change_keyword; // 변수 자료형
   static String before[] = new String[10000]; // 이전 코드
   static int before_length=0;
   static int function_f; // change 포함하는 함수 시작 부분
   
   
   static String[] operator = new String[] { "*", "/", "+", "-", "%", "=" }; // 연산자
   static int change_arr_ind = 0;

   static String left_comment,right_comment;//주석 이전, 이후
   static boolean Isnowcomment=false;
   
   // 3) change_name 이름 설정
   public static String change_name_set(String getname)
   {

      if(getname==null)
      {      
         change = textCheckPage.selectedText;//드래그 된 줄 입력
         change_arr = new String[100]; 
         ecs.change_arr_set();// change 변수 배열 설정 
         Scanner s = new Scanner(System.in);
         
         // 변수 이름 설정
         change_name = "";
         for (int i = 0; i < change_arr_ind; i++)
            change_name = change_name.concat(change_arr[i]);
       }
      else
         change_name=getname;
      change_keyword="int";//임시로 자료형 int로 고정
      return change_name;
   }
   
   // 4) change_arr 설정
   public static void change_arr_set() {
      StringTokenizer st = new StringTokenizer(change, " +-*/%=");// 연산자와 띄어쓰기로 토큰분리
      change_arr_ind=0;
      // 해당 연산식에 연관되는 변수 추출
      while (st.hasMoreTokens())// 입력 파일 안에 문장이 있는 경우
      {
         String token = st.nextToken();
         boolean opera = false;

         for (int i = 0; i < operator.length; i++) {
            // 연산자라면
            if (token == operator[i]) {
               opera = true;
            }
         }
         if (!opera)// 연산자가 아니면 -> 함수나 변수
         {
            change_arr[change_arr_ind++] = token;
         }
      }
   }

   // 7) 시작하기 전 초기화 및 기타 작엄
   public static void start() {
      
      // 바꿀 변수 change set
      change_page = textCheckPage.lineNum; //바꿀 줄 수 입력 (드래그 된 줄 수)
      //change = textCheckPage.selectedText;//드래그 된 줄 입력
      //ecs.change_name_set();// 이름 설정

      res_ind=0;
      result = new String[10000];
      f = 0;
      l=0;
      
      Stack s = new Stack();
      for (page = 0; page < change_page; page++) {
         String now_page = before[page];// 현재 줄
         if (now_page.contains("{")) {
            if (s.empty())
               function_f = page;
            s.push(1);
            f = page;
         }
         if (now_page.contains("}"))
            s.pop();
      }
      Stack s1 = new Stack();
      s1.push(1);
      for(page=page; page<before_length; page++){
         String now_page = before[page];// 현재 줄
         if (now_page.contains("{"))
            s1.push(1);
         if (now_page.contains("}"))
         {
            s1.pop();
            if(s1.empty())
               break;
         }
      }
      l=page;
   }

   // 8) change_name으로 변경될 식이 있는지 확인
   public static boolean can_change(String now_page){
      String str=change;

      //공백 없애기
      if(now_page.contains(change_arr[0]))//변수가 있다면
      {
         System.out.println(change_arr[0].length());
         now_page=now_page.substring(now_page.indexOf(change_arr[0])-change_arr[0].length(),now_page.length());

         now_page = now_page.replaceAll("\\p{Space}", "");
         str = str.replaceAll("\\p{Space}", "");
         
         for(int i=0;i<str.length();i++)
         {
            if(now_page.length()<i)
               break;
            if(now_page.charAt(i)!= str.charAt(i))
               return false;
         }
         return true;
      }
      return false;
   }

   // 9) 좌면이 change_arr[]에 포함되는 경우 판단
   public static boolean left_have(String now_page) 
   {
      if(now_page.contains("="))
      {
         StringTokenizer s=new StringTokenizer(now_page," ");
         String temp=s.nextToken("=").trim();
         for(int i=0;i<change_arr_ind;i++)
            if(temp.matches(change_arr[i]))
               return true;
      }
      return false;
   }

   // 10) 주석 포함된 경우 주석 왼쪽 오른쪽 구분
   public static void comment(String now_page)
   {
      left_comment=now_page.substring(0,now_page.indexOf("//"));//주석 이전 까지 자름(검사하는데 까지 자름)
      right_comment=now_page.substring(now_page.indexOf("//"),now_page.length());//주석 이전 까지 자름(검사하는데 까지 자름)
   }
   
   public void finalize() throws Throwable
   {
      System.out.println("소멸!");
      super.finalize();
   }
   
   public String getChange()
   {
	   return change;
   }
   
   public static String getresult(String before_All){

      System.out.println("이전코드");
     System.out.println(before_All); 
     System.out.println("이전 코드 끝");
     StringTokenizer s=new StringTokenizer(before_All,"\n");

     
      page = 0;
      before = new String[10000];
      before_length=0;      // 바꾸기 이전 코드 before 세팅
      
      while (s.hasMoreTokens())// 입력 파일 안에 문장이 있는 경우
      { 
        String str = s.nextToken();// 한 줄 읽어오기
         before[page++] = str;
      }
      before_length=page;

     // 초기화
     start();
         
     
      //판단 할 필요 없는 부분 전부 집어넣기
      for(page=0;page<=function_f;page++)
         result[res_ind++]=before[page];
      
      result[res_ind++]=change_keyword+" "+change_name+";";//변수 선언
      
      for(page=page;page<change_page;page++)
         result[res_ind++]=before[page];
      
      boolean Isfirst=true;
      boolean Iscomment=false;
      for(page=page;page<l;page++)
      {
         String now=before[page];//현재 줄
         Iscomment=false;

         if(now.contains("/*"))
            Isnowcomment=true;
         
         if(Isnowcomment)//주석이면
         {
            result[res_ind]=now;
         }
         
         else{
            //주석 포함이면
            if(now.contains("//"))
            {
               comment(now);
               Iscomment=true;
               now=left_comment;
            }
         
            //변경될 식이 있다면
            if(can_change(now))
            {
               //처음이면 변수 선언
               if(Isfirst)
               {
                  Isfirst=false;
                  result[res_ind++]=change_name+" = "+change+";";
               }
               now=now.replaceAll("\\p{Space}","");//공백없애기
               String temp=change.replaceAll("\\p{Space}","");//공백없애기
               now=now.replace(temp,change_name);
            }
         
            if(left_have(now))
            {
               result[res_ind++]=before[page++];
               break;
            }
            result[res_ind]=now;
            //주석인경우
            if(Iscomment)
               result[res_ind]=result[res_ind].concat(right_comment);//주석 이후 부분도 추가
         }
         
         res_ind++;
         if(now.contains("*/"))
            Isnowcomment=false;
      }
      while(page<before_length)
         result[res_ind++]=before[page++];
      
      //결과 출력
      String resultt=new String();
      for(int i=0;i<res_ind;i++)
      {
         resultt += (result[i]+"\n");
      }
      return resultt;
   }
}