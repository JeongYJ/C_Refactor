package c_refactor.codes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

import c_refactor.swt.Eliminate_Next;
import c_refactor.swt.textCheckPage;

public class edc {

   static HashMap<String, String> function = new HashMap<String, String>();
   static HashMap<String, String> variable = new HashMap<String, String>();
   static HashMap<String, String> select = new HashMap<String, String>();
   static String before[] = new String[10000000];
   static String result[] = new String[10000000];
   static int page = 0;
   static int res_ind = 0;
   static int page_length = 0;
   static String name;
   static String where;
   static String[] keyword = new String[] { "int", "float", "void" };
   static int use_vind=0;
   static int use_find=0;
   static String function_name[]=new String[10000];
   static String variable_name[]=new String[10000];
   static int f,l;
   static int function_page;
   static int main_page;
   
   static String left_comment,right_comment;//�ּ� ����, ����
   static boolean Isnowcomment=false;

   static List<String> delFunction;
   static List<String> delVariable;
   
   static void set_fl()
   {
      Stack s=new Stack();
      int i;
      for(i=function_page;i<page_length;i++)
      {
         String now=before[i];
         if(now.contains("{"))
            s.push(1);
         if(now.contains("}"))
         {
            s.pop();
            if(s.empty())
               break;
         }
      }
      f=function_page;
      l=i;
   }
   
   // 10) �ּ� ���Ե� ��� �ּ� ���� ������ ����
      public static void comment(String now_page)
      {
         left_comment=now_page.substring(0,now_page.indexOf("//"));//�ּ� ���� ���� �ڸ�(�˻��ϴµ� ���� �ڸ�)
         right_comment=now_page.substring(now_page.indexOf("//"),now_page.length());//�ּ� ���� ���� �ڸ�(�˻��ϴµ� ���� �ڸ�)
      }
   
   //�Լ� �������� �ƴ��� ����
   static boolean Isdeclare_fun(String now, int now_where)
   {
      now = now.trim();
      StringTokenizer s = new StringTokenizer(now, " ");
      String temp_s;
      if(!now.contains("("))//->����
         return false;
      if (!s.hasMoreTokens())
         return false;
      temp_s = s.nextToken();
      for (int i = 0; i < keyword.length; i++) {
         
         // Ű���� �����ϰ� ������
         if (temp_s.matches(keyword[i])) {
            //System.out.println(page);
            temp_s = s.nextToken(" ");
            temp_s=temp_s.substring(0,temp_s.indexOf("("));
            name = temp_s;
            
            //main �Լ� ����
            if(name.matches("main"))
            {
               System.out.println("main page : "+now_where);
               main_page=now_where;
               return false;
            }
            where = Integer.toString(now_where);
            Stack s1=new Stack();
            int temp_page=page;
            for(temp_page=page;temp_page<page_length;temp_page++)
            {
               String now_page=before[temp_page];
               if(now_page.contains("{"))
                  s1.push(1);
               if(now_page.contains("}"))
               {
                  s1.pop();
                  if(s1.empty())
                  {
                     break;
                  }
               }
            }
            where=where.concat(" "+Integer.toString(temp_page));
            
            function_name[use_find++]=name;
            return true;
         }
      }
      return false;
   }
   
   // ���� �������� �ƴ��� ����
   static boolean Isdeclare_var(String now,String what) {
      
      now = now.trim();
      StringTokenizer s = new StringTokenizer(now, " ");
      String temp_s;
      if(now.contains("("))//->�Լ�
         return false;
      if (!s.hasMoreTokens())
         return false;
      temp_s = s.nextToken();
      
      
      for (int i = 0; i < keyword.length; i++) {
         // Ű���� �����ϰ� ������
         if (temp_s.matches(keyword[i])) {
            if(what.matches("plus"))
            {
               temp_s = s.nextToken(" ");
               if(temp_s.contains(";"))
                  temp_s = temp_s.replaceAll(";","");
               name = temp_s;
               where = Integer.toString(res_ind);
               variable_name[use_vind++]=name;
            }
            return true;
         }
      }
      return false;
   }

   //�Լ� ��Ŀ��
   static void function_focus() {

      function_page = textCheckPage.lineNum;
      
      set_fl();
      
      Stack s = new Stack();

      for (page = 0; page < function_page; page++)
         result[res_ind++] = before[page];
      
      boolean Iscomment=false;
      
      for (page = f; page < l; page++) {
         String now = before[page];
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
         
            if (now.contains("{"))
            {
               s.push(1);
            }
            if (now.contains("}")) {
               s.pop();
               if (s.empty()) {
                  result[res_ind++] = before[page++];
                  break;
               }
            }
         
            // ���������̸�
            if (Isdeclare_var(now,"plus")) {
               variable.put(name, where);
            }    
         
            else 
            {
               if (now.contains("return"))
               {   //������ return �� �� ���
                  if (s.size() == 1) 
                  {
                     result[res_ind++] = before[page++];
                     result[res_ind++] = "}";
                     Iterator<String> iterator=variable.keySet().iterator();
                     while(iterator.hasNext())
                     {
                        String key=iterator.next();
                        // �ش� ���� ���
                        if (now.contains(key))
                        {
                           variable.replace(key, "0");
                           break;
                        }
                     }
                     break;
                     }
                  }
                  Iterator<String> iterator=variable.keySet().iterator();
                  while(iterator.hasNext())
                  {
                  String key=iterator.next();
               
                  // �ش� ���� ���
                  if (now.contains(key))
                  {
                     //���� key�� ��ġ�� =���� �����ʿ� �ִ� ���
                     if((now.contains("=") && now.indexOf(key)>now.indexOf("="))
                        || now.contains("for") || now.contains("if") || now.contains("while") || now.contains("return"))
                     {   
                        variable.replace(key, "0");
                        break;
                     }
                     //���� key�� ��ġ�� =���� ���ʿ� �ִ� ���
                     else
                     {
                        if(now.contains("=") && now.indexOf(key)<now.indexOf("="))
                        {
                           //System.out.println("= �º�");
                        }
                        //++,-- �� �����̳� �����ʿ� key�� ������ ���
                        else if(now.contains("++")&&(now.indexOf(key)==now.indexOf("++")-key.length() ||now.indexOf(key)==now.indexOf("++")+2)  )
                        {
                           //System.out.println("���� ����");
                        }
                        else if(now.contains("--")&&(now.indexOf(key)==now.indexOf("--")-key.length() ||now.indexOf(key)==now.indexOf("--")+2))
                        {
                           //System.out.println("���� ����");
                        }
                        else
                        {
                           //���Ǿ��ٰ� �Ǵ�
                           variable.replace(key, "0");
                           break;
                        }
                     }
                     //variable.remove(key);// ���� ����
                  }
               }
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
      for(int i=l+1;i<page_length;i++)
         result[res_ind++] = before[i];
      for(int i=0;i<use_vind;i++)
      {
         //��� �� �� ����->��� �ϴ� �κ� �� ����
         if(!variable.get(variable_name[i]).matches("0"))
         {
            result[Integer.parseInt(variable.get(variable_name[i]))]="";

            String key=variable_name[i];
            for(int j=f;j<l;j++)
            {
               String now=result[j];
               Iscomment=false;

               if(now==null)
                  break;
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
               
                  //���� ������ �ƴ� ���
                  if (!Isdeclare_var(now,"no")&&now.contains(key)&& (!now.contains("for") && !now.contains("if") && !now.contains("while") && !now.contains("return")))
                  {
                     if(now.contains("=") && now.indexOf(key)<now.indexOf("="))
                     {
                        result[j]="";
                     }
                     //++,-- �� �����̳� �����ʿ� key�� ������ ���
                     else if(now.contains("++")&&(now.indexOf(key)==now.indexOf("++")-key.length() ||now.indexOf(key)==now.indexOf("++")+2)  )
                     {
                        result[j]="";
                     }
                     else if(now.contains("--")&&(now.indexOf(key)==now.indexOf("--")-key.length() ||now.indexOf(key)==now.indexOf("--")+2))
                     {
                        result[j]="";
                     }
                  }
               }
               if(now.contains("*/"))
                  Isnowcomment=false;
            }

            delVariable.add(key);
         }
      }
      getDelVariable();
   }
   
   //�Լ� �ȿ��� ��������, ���� �Լ� ��� ���� Ȯ��
   //startpage = �Լ� ���� , lastpage = �Լ� ����
   static void check_function_inside(int startpage,int lastpage)
   {
      //�Լ� ���ۺ��� ������ Ȯ��
      for(int page=startpage;page<lastpage;page++)
      {
         String now=before[page];
         
         //�Լ� ��� ����
          Iterator<String> iterator=function.keySet().iterator();
          while(iterator.hasNext())
          {
             String key=iterator.next();
                 
              // �ش� ���� ��� 
              if (!function.get(key).matches("0") && now.contains(key))
              {
                 String temp = function.get(key);
                 StringTokenizer s=new StringTokenizer(temp," ");

                 String str=s.nextToken();
                 int st = Integer.parseInt(str);
                 str=s.nextToken();
                 int la = Integer.parseInt(str);
                 
                 function.replace(key, "0"); //�Լ� ����ߴٰ� ǥ��
                 check_function_inside(st,la);
              }
          }

          //�������� ��� ���� Ȯ��
          iterator=variable.keySet().iterator();
          while(iterator.hasNext())
          {
             String key=iterator.next();
              // �ش� ���� ���
              if (now.contains(key))
              {
                 variable.replace(key, "0");
              }
           }
       }
   }   
   
   //��ü�ڵ� ��Ŀ��
   static void all_focus() {
      Stack s = new Stack();
      for(page=0;page<page_length;page++)
      {   
         String now=before[page];
         if(now.contains("{"))
            s.push(1);
         if(now.contains("}"))
            s.pop();
         
         //���� �κ�
         if(s.empty())
         {
            if(Isdeclare_fun(now,page))
            {
               function.put(name, where);
            }
            else if(Isdeclare_var(now,"plus"))
            {
               variable.put(name, where);
            }
         }
      }
      
      check_function_inside(main_page,page_length);
      
      for(int i=0;i<use_find;i++)
      {
         //��� �� �� �Լ��̸�
         if(!function.get(function_name[i]).matches("0"))
         {
            int left,right;
            String str = function.get(function_name[i]);
            
            delFunction.add(function_name[i]);
            
            System.out.println("������ �Լ� �̸� : " + str);
            StringTokenizer st=new StringTokenizer(str," ");
            
            left=Integer.parseInt(st.nextToken().toString());
            right=Integer.parseInt(st.nextToken().toString());
            for(int j=left;j<=right;j++)
               before[j]="";
         }
      }
      for(int i=0;i<use_vind;i++)
      {
         //��� �� �� �����̸�
         if(!variable.get(variable_name[i]).matches("0"))
         {
            before[Integer.parseInt(variable.get(variable_name[i]))]="";
            
             delVariable.add(variable_name[i]);
         }
      }
      
      getDelFunction();
      getDelVariable();
   }

   public void finalize() throws Throwable
   {
      System.out.println("�Ҹ�!");
      super.finalize();
   }
   
   public static void start()
   {
      function = new HashMap<String, String>();
      variable = new HashMap<String, String>();
      select = new HashMap<String, String>();
      before = new String[10000000];
      result = new String[10000000];
      delFunction=new ArrayList<String>();
      delVariable=new ArrayList<String>();
      
      page = 0;
      res_ind = 0;
      page_length = 0;
      Isnowcomment=false;
      keyword = new String[] { "int", "float", "void" };
      use_vind=0;
      use_find=0;
      function_name=new String[10000];
      variable_name=new String[10000];
   }
   
   
   
   //������ �Լ� ��ȯ
   public static List getDelFunction()
   {
      System.out.println("delFunction.size() : "+delFunction.size());
      Eliminate_Next.elements = null;
      //for(int i=0;i<delFunction.size();i++)
      //   System.out.println("�Լ� "+delFunction.get(i)+" ����");
      return delFunction;
   }

   //������ ���� ��ȯ
   public static List getDelVariable()
   {
      System.out.println("delVariable.size() : "+delVariable.size());
      Eliminate_Next.elements = null;
     // for(int i=0;i<delVariable.size();i++)
     // {
      //   Eliminate_Next.elements = delVariable.get(i);
      //   System.out.println("���� "+Eliminate_Next.elements+" ����");
     // }
      return delVariable;
   }
   
   
   
   public static String getresult(String before_All) {
      StringTokenizer s=new StringTokenizer(before_All,"\n");
       
      start();//���� �ʱ�ȭ
      
      page = 0;
     // �ٲٱ� ���� �ڵ� before ����
      while (s.hasMoreTokens())// �Է� ���� �ȿ� ������ �ִ� ���
      { 
         String str = s.nextToken();// �� �� �о����
          before[page++] = str;
      }
      // �����丵 ���� �ڵ� �ֱ�
      
      page_length = page;

      String resultt=new String();
      
      
      //�巡�� �� �� ���ٸ� ��ü �ڵ� �Լ� ȣ��
      if(textCheckPage.selectedText.length()==0)
      {
         System.out.println("��ü�ڵ�");
         all_focus();
         
         for(int i=0;i<page_length;i++)
             resultt+= (before[i]+"\n");
       
      }
      else
      {
         System.out.println("�Լ��ڵ�");
         function_focus();
         
         for(int i=0;i<res_ind;i++)
            resultt+= (result[i]+"\n");
         
      }
      
      
      return resultt;
   }
}