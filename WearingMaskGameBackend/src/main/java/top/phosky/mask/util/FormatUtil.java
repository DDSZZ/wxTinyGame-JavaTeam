package top.phosky.mask.util;

import javax.swing.tree.TreeNode;
import java.util.Iterator;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.Normalizer;
import java.util.*;

/***
 * It is a utility class to format the output
 *
 * @author Phosky, NEU
 * @version 3.3
 */
public class FormatUtil {
    //由笔画(F,S,T,M)_方向(U,D,L,R)来确定
    private final static char F_R = '─';
    private final static char F_D = '│';
    private final static char F_D_V = '┊';
    private final static char S_RD = '┌';
    private final static char S_LD = '┐';
    private final static char S_LU = '┘';
    private final static char S_RU = '└';
    private final static char T_LDR = '┬';
    private final static char T_ULD = '┤';
    private final static char T_LUR = '┴';
    private final static char T_URD = '├';
    private final static char M_URDL = '┼';
    public static String NEW_LINE = "\r\n";//换行符。可以根据不同操作系统手动修改

    private static FormatUtil singleton;

    private FormatUtil() {
    }

    public static FormatUtil getSingleton() {
        if (singleton == null) {
            singleton = new FormatUtil();
        }
        return singleton;
    }

    // 判断一个字符是否是中文汉字或字符
    private boolean isChinese(char c) {
        //特殊情况
        switch (c) {
            case '—':
                return false;
        }
        // 根据UnicodeBlock方法判断中文标点符号
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
                || ub == Character.UnicodeBlock.VERTICAL_FORMS) {
            return true;
        }
        return c >= 0x4E00 && c <= 0x9FA5;// 根据字节码判断
    }

    //得到一个字符串在Console字体显示上的‘长度’
    public int lengthInView(String content) {
        int length = 0;
        for (char c : content.toCharArray()) {
            if (isChinese(c)) {//中文长度为2，加两次
                length++;
            }
            length++;
        }
        return length;
    }

    /**
     * Format Object for easy display
     * <p>
     * If the length is not enough, a "~ " will be added at the end
     *
     * @param
     * @return String : Fixed-length string
     */
    public <T> String formatObject(int length, T obj) {
        String s = String.valueOf(obj);
        if (obj == null) {
            s = "";
        }
        StringBuilder sb = new StringBuilder();
        boolean hadReached = false;//是否已经超长了
        char[] sChar = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            sb.append(sChar[i]);
            if (lengthInView(sb.toString()) >= length) {
                sb.deleteCharAt(sb.length() - 1);
                hadReached = true;
                break;
            }
        }
        int stillNeed = length - lengthInView(sb.toString());
        while (lengthInView(sb.toString()) != length) {
            if (stillNeed > 0) {
                //已达到的添加'~'，未达到的添加' '
                if (hadReached) {
                    sb.append('~');
                } else {
                    sb.append(' ');
                }
            } else if (stillNeed == 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        return sb.toString();
    }

    //多次输出某一字符串
    public String multiString(String s, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    //输出表格，不定长列宽
    public <T> String formatTable(String[] headRowNames, T[][] matrix, int[] columnLengths) {
        StringBuilder sb = new StringBuilder();
        int height = matrix.length;
        int maxWidth = headRowNames.length;
        {//第一行
            sb.append(FormatUtil.S_RD);
            for (int i = 0; i < maxWidth - 1; i++) {
                sb.append(multiString(String.valueOf(FormatUtil.F_R), columnLengths[i]));
                sb.append(FormatUtil.T_LDR);
            }
            sb.append(multiString(String.valueOf(FormatUtil.F_R), columnLengths[maxWidth - 1]));
            sb.append(FormatUtil.S_LD);
            sb.append(FormatUtil.NEW_LINE);
        }
        {//表头
            {//输出数据
                if (headRowNames.length >= 1) {
                    sb.append(FormatUtil.F_D);
                    String s = formatObject(columnLengths[0], headRowNames[0]);
                    sb.append(s);
                    sb.append(FormatUtil.F_D);
                }
                for (int j = 1; j < maxWidth; j++) {
                    String s = formatObject(columnLengths[j], headRowNames[j]);
                    sb.append(s);
                    sb.append(FormatUtil.F_D);
                }
                sb.append(FormatUtil.NEW_LINE);
            }
            {//输出中线
                sb.append(FormatUtil.T_URD);
                for (int k = 0; k < maxWidth - 1; k++) {
                    sb.append(multiString(String.valueOf(FormatUtil.F_R), columnLengths[k]));
                    sb.append(FormatUtil.M_URDL);
                }
                sb.append(multiString(String.valueOf(FormatUtil.F_R), columnLengths[maxWidth - 1]));
                sb.append(FormatUtil.T_ULD);
                sb.append(FormatUtil.NEW_LINE);
            }

        }
        for (int i = 0; i < height; i++) {
            {//输出数据
                if (matrix[i].length >= 1) {
                    sb.append(FormatUtil.F_D);
                    String s = formatObject(columnLengths[0], matrix[i][0]);
                    sb.append(s);
                    sb.append(FormatUtil.F_D);
                }
                for (int j = 1; j < maxWidth; j++) {
                    String s = formatObject(columnLengths[j], matrix[i][j]);
                    sb.append(s);
                    sb.append(FormatUtil.F_D);
                }
                sb.append(FormatUtil.NEW_LINE);
            }
            if (i == height - 1) {
                {//输出底线
                    sb.append(FormatUtil.S_RU);
                    for (int k = 0; k < maxWidth - 1; k++) {
                        sb.append(multiString(String.valueOf(FormatUtil.F_R), columnLengths[k]));
                        sb.append(FormatUtil.T_LUR);
                    }
                    sb.append(multiString(String.valueOf(FormatUtil.F_R), columnLengths[maxWidth - 1]));
                    sb.append(FormatUtil.S_LU);
                    sb.append(FormatUtil.NEW_LINE);
                }
            } else {
                {//输出中线
                    sb.append(FormatUtil.T_URD);
                    for (int k = 0; k < maxWidth - 1; k++) {
                        sb.append(multiString(String.valueOf(FormatUtil.F_R), columnLengths[k]));
                        sb.append(FormatUtil.M_URDL);
                    }
                    sb.append(multiString(String.valueOf(FormatUtil.F_R), columnLengths[maxWidth - 1]));
                    sb.append(FormatUtil.T_ULD);
                    sb.append(FormatUtil.NEW_LINE);
                }
            }
        }
        return sb.toString();
    }

    //输出表格，定长度列宽
    public <T> String formatTable(String[] headRowNames, T[][] matrix, int everyColumnLength) {
        int[] columnLengths = new int[headRowNames.length];
        for (int i = 0; i < headRowNames.length; i++) {
            columnLengths[i] = everyColumnLength;
        }
        return formatTable(headRowNames, matrix, columnLengths);
    }

    //输出表格，集合，不定长列宽
    public <T> String formatTable(List<String> headRowNames, List<List<T>> data, List<Integer> columnLengths) {
        int length = headRowNames.size();
        String[] headRowNamesArray = new String[length];
        int i = 0;
        for (String s : headRowNames) {
            headRowNamesArray[i] = s;
            i++;
        }
        Object[][] matrix = new Object[data.size()][length];
        i = 0;
        for (List<T> l : data) {
            int j = 0;
            for (T t : l) {
                matrix[i][j] = t;
                j++;
            }
            i++;
        }
        int[] columnLengthsArray = new int[length];
        i = 0;
        for (Integer num : columnLengths) {
            columnLengthsArray[i] = num;
            i++;
        }
        return formatTable(headRowNamesArray, matrix, columnLengthsArray);
    }

    //输出表格，集合，定长列宽
    public <T> String formatTable(List<String> headRowNames, List<List<T>> data, int columnLengths) {
        List<Integer> cLs = new LinkedList<>();
        for (int i = 0; i < headRowNames.size(); i++) {
            cLs.add(columnLengths);
        }
        return formatTable(headRowNames, data, cLs);
    }

    //格式化输出树结构，名为toStringMethodName的方法(传入参数为空)需要返回单行字符串，子节点集合要求使用List家族
    public <T> String formatTree(String childrenFieldName, String toStringMethodName, int tabSize, T treeRootNodeObj) {
        class Inner<T> {
            public Field childrenField;
            public Method beatyPrintMethod;

            public Inner() {
                try {
                    Class<?> treeNodeClass = treeRootNodeObj.getClass();
                    this.childrenField = treeNodeClass.getDeclaredField(childrenFieldName);
                    childrenField.setAccessible(true);
                    this.beatyPrintMethod = treeNodeClass.getDeclaredMethod(toStringMethodName);
                    beatyPrintMethod.setAccessible(true);
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }

            public String getSpaceAtFront(Stack<Boolean> isNeedVirtualLine) {
                StringBuilder sb = new StringBuilder();
                Iterator<Boolean> it = isNeedVirtualLine.backIterator();
                while (it.hasNext()) {
                    sb.insert(0, multiString(" ", tabSize - 1));
                    if (it.next()) {
                        sb.insert(0, FormatUtil.F_D_V);
                    } else {
                        sb.insert(0, ' ');
                    }
                }
                return sb.toString();
            }

            public String printNode(T current, boolean isFirst, int depth, boolean isEndOne, Stack<Boolean> isNeedVirtualLine) {
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append(getSpaceAtFront(isNeedVirtualLine));//空格
                    if (isFirst) {//左上角
                        sb.append(FormatUtil.S_RD);
                    } else {
                        sb.append(FormatUtil.T_URD);
                    }
                    String toString = (String) beatyPrintMethod.invoke(current, null);
                    int length = lengthInView(toString);
                    if (length <= tabSize) {
                        length = tabSize + 1;
                    }
                    sb.append(multiString(String.valueOf(FormatUtil.F_R), length)).append(FormatUtil.S_LD).append(FormatUtil.NEW_LINE);
                    sb.append(getSpaceAtFront(isNeedVirtualLine));//空格
                    sb.append(FormatUtil.F_D).append(toString).append(FormatUtil.F_D).append(FormatUtil.NEW_LINE);
                    sb.append(getSpaceAtFront(isNeedVirtualLine));//空格
                    isNeedVirtualLine.push(!isEndOne);//入栈
                    if (isEndOne) {//左下角
                        sb.append(FormatUtil.S_RU);
                    } else {
                        sb.append(FormatUtil.T_URD);
                    }
                    sb.append(multiString(String.valueOf(FormatUtil.F_R), tabSize - 1));
                    List<T> children = getChildren(current);
                    if (children == null) {
                        sb.append(FormatUtil.F_R);
                    } else {//有分支元素
                        sb.append(FormatUtil.T_LDR);
                    }
                    int leftLength = length - tabSize;
                    sb.append(multiString(String.valueOf(FormatUtil.F_R), leftLength)).append(FormatUtil.S_LU).append(FormatUtil.NEW_LINE);
                    if (children != null) {
                        int j = 0;
                        int endOne = children.size() - 1;
                        for (T t : children) {
                            if (j == endOne) {
                                sb.append(printNode(t, false, depth + 1, true, isNeedVirtualLine));
                            } else {
                                sb.append(printNode(t, false, depth + 1, false, isNeedVirtualLine));
                            }
                            j++;
                        }
                    }
                    isNeedVirtualLine.pop();//出栈
                    return sb.toString();
                } catch (Exception err) {
                    err.printStackTrace();
                }
                return null;
            }

            //得到孩子的集合
            public List<T> getChildren(T current) {
                try {
                    return (List<T>) childrenField.get(current);
                } catch (Exception err) {
                    err.printStackTrace();
                }
                return null;
            }
        }
        //以上是内部类
        try {
            Inner<T> inner = new Inner<>();
            return inner.printNode(treeRootNodeObj, true, 0, true, new Stack<>());
        } catch (Exception err) {
            err.printStackTrace();
        }
        return null;
    }

    //带边框文本段
    public String formatTextArea(String[] content) {
        int maxLength = 0;//寻找最大长度
        for (int i = 0; i < content.length; i++) {
            int lengthNow = lengthInView(content[i]);
            if (lengthNow > maxLength) {
                maxLength = lengthNow;
            }
        }
        StringBuilder sb = new StringBuilder();
        //DEBUG: maxLength+1，防止结尾被显示成~
        {//第一行
            sb.append(FormatUtil.S_RD);
            sb.append(multiString(String.valueOf(FormatUtil.F_R), maxLength + 1));
            sb.append(FormatUtil.S_LD);
            sb.append(FormatUtil.NEW_LINE);
        }
        {//中间行
            for (int i = 0; i < content.length; i++) {
                sb.append(FormatUtil.F_D);
                sb.append(formatObject(maxLength + 1, content[i]));
                sb.append(FormatUtil.F_D);
                sb.append(FormatUtil.NEW_LINE);
            }
        }
        {//结尾行
            sb.append(FormatUtil.S_RU);
            sb.append(multiString(String.valueOf(FormatUtil.F_R), maxLength + 1));
            sb.append(FormatUtil.S_LU);
        }
        return sb.toString();
    }

    //带边框文本域，集合
    public String formatTextArea(List<String> content) {
        String[] array = new String[content.size()];
        int i = 0;
        for (Object o : content.toArray()) {
            array[i] = (String) o;
            i++;
        }
        return formatTextArea(array);
    }
}


/**
 * 栈。包含反向迭代器
 *
 * @author Phosky, NEU
 * @version 0.9
 */
class Stack<T> {
    private Node<T> tail;

    public void push(T t) {
        Node<T> n = new Node<>(t);
        n.next = tail;
        tail = n;
    }

    public T top() {
        return tail.data;
    }

    public void pop() {
        tail = tail.next;
    }

    public boolean isEmpty() {
        return tail == null;
    }

    public Iterator<T> backIterator() {
        Node<T> tailPre = this.tail;
        Iterator<T> it = new Iterator<T>() {
            Node<T> nodeNow = tailPre;

            @Override
            public boolean hasNext() {
                return nodeNow != null;
            }

            @Override
            public T next() {
                Node<T> nodeTemp = nodeNow;
                nodeNow = nodeNow.next;
                return nodeTemp.data;
            }
        };
        this.tail = tailPre;
        return it;
    }

    @SuppressWarnings("hiding")
    class Node<T> {
        public Node<T> next;
        public T data;

        public Node(T data) {
            super();
            this.data = data;
        }
    }
}
