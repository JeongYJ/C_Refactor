package c_refactor.codes;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

import c_refactor.swt.textCheckPage;
import c_refactor.swt.Rename_;


public class ecs {

   static String change; // x * y
   static String change_arr[] = new String[1000]; // change�� ���� ���� ����
   static String change_name; // xy
   static int page = 0; // �˻��ϴ� �� ��
   static int f, l = 0; // �˻� ���� �� ��,�� �� ��
   static String result[] = new String[10000]; // �����丵 ��� ���� �迭
   static int res_ind = 0; // result index
   static int change_page; // change �ش� �� ��
   static String change_keyword; // ���� �ڷ���
   static String before[] = new String[10000]; // ���� �ڵ�
   static int before_length=0;
   static int function_f; // change �����ϴ� �Լ� ���� �κ�
   
   
   static String[] operator = new String[] { "*", "/", "+", "-", "%", "=" }; // ������
   static int change_arr_ind = 0;

   static String left_comment,right_comment;//�ּ� ����, ����
   static boolean Isnowcomment=false;
   
   // 3) change_name �̸� ����
   public static String change_name_set(String getname)
   {

      if(getname==null)
      {      
         change = textCheckPage.selectedText;//�巡�� �� �� �Է�
         change_arr = new String[100]; 
         ecs.change_arr_set();// change ���� �迭 ���� 
         Scanner s = new Scanner(System.in);
         
         // ���� �̸� ����
         change_name = "";
         for (int i = 0; i < change_arr_ind; i++)
            change_name = change_name.concat(change_arr[i]);
       }
      else
         change_name=getname;
      change_keyword="int";//�ӽ÷� �ڷ��� int�� ����
      return change_name;
   }
   
   // 4) change_arr ����
   public static void change_arr_set() {
      StringTokenizer st = new StringTokenizer(change, " +-*/%=");// �����ڿ� ����� ��ū�и�
      change_arr_ind=0;
      // �ش� ����Ŀ� �����Ǵ� ���� ����
      while (st.hasMoreTokens())// �Է� ���� �ȿ� ������ �ִ� ���
      {
         String token = st.nextToken();
         boolean opera = false;

         for (int i = 0; i < operator.length; i++) {
            // �����ڶ��
            if (token == operator[i]) {
               opera = true;
            }
         }
         if (!opera)// �����ڰ� �ƴϸ� -> �Լ��� ����
         {
            change_arr[change_arr_ind++] = token;
         }
      }
   }

   // 7) �����ϱ� �� �ʱ�ȭ �� ��Ÿ �۾�
   public static void start() {
      
      // �ٲ� ���� change set
      change_page = textCheckPage.lineNum; //�ٲ� �� �� �Է� (�巡�� �� �� ��)
      //change = textCheckPage.selectedText;//�巡�� �� �� �Է�
      //ecs.change_name_set();// �̸� ����

      res_ind=0;
      result = new String[10000];
      f = 0;
      l=0;
      
      Stack s = new Stack();
      for (page = 0; page < change_page; page++) {
         String now_page = before[page];// ���� ��
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
         String now_page = before[page];// ���� ��
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

   // 8) change_name���� ����� ���� �ִ��� Ȯ��
   public static boolean can_change(String now_page){
      String str=change;

      //���� ���ֱ�
      if(now_page.contains(change_arr[0]))//������ �ִٸ�
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

   // 9) �¸��� change_arr[]�� ���ԵǴ� ��� �Ǵ�
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

   // 10) �ּ� ���Ե� ��� �ּ� ���� ������ ����
   public static void comment(String now_page)
   {
      left_comment=now_page.substring(0,now_page.indexOf("//"));//�ּ� ���� ���� �ڸ�(�˻��ϴµ� ���� �ڸ�)
      right_comment=now_page.substring(now_page.indexOf("//"),now_page.length());//�ּ� ���� ���� �ڸ�(�˻��ϴµ� ���� �ڸ�)
   }
   
   public void finalize() throws Throwable
   {
      System.out.println("�Ҹ�!");
      super.finalize();
   }
   
   public String getChange()
   {
	   return change;
   }
   
   public static String getresult(String before_All){

      System.out.println("�����ڵ�");
     System.out.println(before_All); 
     System.out.println("���� �ڵ� ��");
     StringTokenizer s=new StringTokenizer(before_All,"\n");

     
      page = 0;
      before = new String[10000];
      before_length=0;      // �ٲٱ� ���� �ڵ� before ����
      
      while (s.hasMoreTokens())// �Է� ���� �ȿ� ������ �ִ� ���
      { 
        String str = s.nextToken();// �� �� �о����
         before[page++] = str;
      }
      before_length=page;

     // �ʱ�ȭ
     start();
         
     
      //�Ǵ� �� �ʿ� ���� �κ� ���� ����ֱ�
      for(page=0;page<=function_f;page++)
         result[res_ind++]=before[page];
      
      result[res_ind++]=change_keyword+" "+change_name+";";//���� ����
      
      for(page=page;page<change_page;page++)
         result[res_ind++]=before[page];
      
      boolean Isfirst=true;
      boolean Iscomment=false;
      for(page=page;page<l;page++)
      {
         String now=before[page];//���� ��
         Iscomment=false;

         if(now.contains("/*"))
            Isnowcomment=true;
         
         if(Isnowcomment)//�ּ��̸�
         {
            result[res_ind]=now;
         }
         
         else{
            //�ּ� �����̸�
            if(now.contains("//"))
            {
               comment(now);
               Iscomment=true;
               now=left_comment;
            }
         
            //����� ���� �ִٸ�
            if(can_change(now))
            {
               //ó���̸� ���� ����
               if(Isfirst)
               {
                  Isfirst=false;
                  result[res_ind++]=change_name+" = "+change+";";
               }
               now=now.replaceAll("\\p{Space}","");//������ֱ�
               String temp=change.replaceAll("\\p{Space}","");//������ֱ�
               now=now.replace(temp,change_name);
            }
         
            if(left_have(now))
            {
               result[res_ind++]=before[page++];
               break;
            }
            result[res_ind]=now;
            //�ּ��ΰ��
            if(Iscomment)
               result[res_ind]=result[res_ind].concat(right_comment);//�ּ� ���� �κе� �߰�
         }
         
         res_ind++;
         if(now.contains("*/"))
            Isnowcomment=false;
      }
      while(page<before_length)
         result[res_ind++]=before[page++];
      
      //��� ���
      String resultt=new String();
      for(int i=0;i<res_ind;i++)
      {
         resultt += (result[i]+"\n");
      }
      return resultt;
   }
}