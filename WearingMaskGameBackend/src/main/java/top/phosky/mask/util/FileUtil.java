package top.phosky.mask.util;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;

// ---------------------------------------------------------------------------------------------------

/**
 * @author Phosky, NEU
 * @version 5.7
 * <p>
 * FUNCTION：
 * <p>
 * 此工具类已经大改，修改了追加序列化为将ArrayList序列化。
 * <p>
 * 这个类是为文件IO操作服务的，提供了读、增、写、删、改、查的方法，支持String、Byte[]和所有对象类型的储存。采用单例模式，防止读写线程交叉。
 * <p>
 * WARNING：
 * <p>
 * 写出无参构造之后才能用FILE_PATH和FIKE_NAME寻找文件路径
 * <p>
 * path参数是所需文件上一级的绝对路径，name是所需文件名，方法中会自动将他们之间加入"\\",不需要请直接把绝对路径输在path里
 * <p>
 * 字符串类型数据中的"\n"在系统默认的记事本中不会显示出来，在"NotePad"里可以看到效果。
 * <p>
 * 字符串类型请不要当作对象进行储存，否则查找、删除功能无效，追加写入也无法检测重复。
 * <p>
 * 读文件的方法，你需要将返回值强制转换为所需要的数据类型。
 * <p>
 * 返回对象的方法可能返回null，你需要在自己的程序中防止空指针异常，具体看方法的介绍。
 * <p>
 * 如果以字符串作为查重属性，很有可能需要进行Clone处理。
 * <p>
 * String的操作为UTF-8格式
 * <p>
 * 判断泛型的类型的方式为传入字节码
 * <p>
 * 另一套参数较少的保存读取是提供给除ArrayList以外的ADT存储的
 * <p>
 * 方法：
 * <p>
 * (static) getSingleton():FileUtil (synchronized < T >)
 * saveFile(String path, String name, T object, Boolean
 * isObject,Boolean isAppend, String key):Integer
 * <p>
 * (synchronized < T >) readFile(String path, String name,Class< T >
 * objClass, Boolean isObject, Boolean isString):Object
 * <p>
 * (synchronized < T >) delItemInObjectFile(String path,String
 * name,Class< T > objClass, String key, Object value):Integer
 * <p>
 * (synchronized < T >) modifyItemInObjectFile(String path, String
 * name, String key, Object value ,T newObject):Integer
 * <p>
 * (synchronized < T >) searchItemInObjectFile(String path, String
 * name,Class< T > objClass, String key, Object value):T
 * <p>
 * (synchronized <T>) saveFile(String absolutePathName, T object):
 * Integer
 * <p>
 * (synchronized <T>) readFile(String absolutePathName) : T
 */
// ---------------------------------------------------------------------------------------------------
public final class FileUtil {
    private static FileUtil fileUtilSingleten;

    private FileUtil() {
    }

    /**
     * 公有静态方法得到单例，之后可以调用方法。
     * <p>
     *
     * @return <p>
     * FileUtil:得到单例。
     */
    public static synchronized FileUtil getSingleton() {
        if (fileUtilSingleten == null) {
            fileUtilSingleten = new FileUtil();
        }
        return fileUtilSingleten;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 一个私有的方法去检测一个文件是否存在，如果不存在会创建。
     * <p>
     * 返回File类型对象。
     *
     * @param
     * @return File:成功
     * <p>
     * null:输入的path和name为空，但是对象里找不到储存位置的两个常量
     */
    private synchronized <T> File fileEmptyDeal(String path, String name, Class<T> objClass) {
        try {
            if (path.trim().equals("") && name.trim().equals("")) {
                T t0;
                Class<T> c = objClass;

                Constructor<T> con = c.getDeclaredConstructor();
                con.setAccessible(true);
                t0 = con.newInstance();
                Field field0;// read FILE_PATH
                try {
                    field0 = c.getDeclaredField("FILE_PATH");
                } catch (NoSuchFieldException e) {
                    return null;
                }
                field0.setAccessible(true);
                path = (String) field0.get(t0);
                Field field1;// read FILE_NAME
                try {
                    field1 = c.getDeclaredField("FILE_NAME");
                } catch (NoSuchFieldException e) {
                    return null;
                }
                field1.setAccessible(true);
                name = (String) field1.get(t0);
            }
            File f = null;
            if (name.trim().equals("")) {
                f = new File(path);
                if (!f.exists()) {
                    File fTemp = new File(f.getParent());
                    if (!fTemp.exists()) {
                        fTemp.mkdirs();// create folder
                    }
                    f.createNewFile();// create file
                }
            } else {
                f = new File(path + "\\" + name);
                if (!f.exists()) {
                    File fTemp = new File(path);
                    if (!fTemp.exists()) {
                        fTemp.mkdirs();// create folder
                    }
                    f.createNewFile();// create file
                }
            }
            return f;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 这是一个可以储存任何文件的方法！
     * <p>
     * 参数：
     * <p>
     * String:所需文件上一级的绝对路径。
     * <p>
     * String:所需文件名。
     * <p>
     * 上两项如果都输入空的话，方法会根据想要储存对象中的(String)FILE_PATH常量和(String)FILE_NAME常量来确认。
     * <p>
     * Object:想要储存的对象
     * <p>
     * Boolean:是否以对象来存储
     * <p>
     * 存储有3种形式：
     * <p>
     * 上一项填true:以对象形式存储
     * <p>
     * 上一项填false,对象为byte[]:以流的形式存储
     * <p>
     * 上一项填false,对象为String:以文本的形式存储（可以说是上一种的特例）
     * <p>
     * Boolean:是否追加写出
     * <p>
     * String:如果为追加写出且为以对象形式储存，则会检测原文件中对象的以这个String的内容作为属性是否有重复，作为查询重复的标识属性。
     *
     * @param
     * @return 0:巨大的错误。
     * <p>
     * 1:保存成功。
     * <p>
     * 2:非对象式储存只针对与byte[]和String类型。
     * <p>
     * 3:相同的对象已经存在。
     * <p>
     * 4:没有这个属性供检测是否追加重复对象。
     * <p>
     * 5:两次追加的对象种类不一致。
     * <p>
     * 6:对象属性中不存在FILE_PATH常量或FILE_NAME常量。
     */
    public synchronized <T> Integer saveFile(String path, String name, T object, Boolean isObject, Boolean isAppend,
                                             String key) {
        try {
            Class<?> c = object.getClass();
            File f = fileEmptyDeal(path, name, c);// test null file
            if (f == null) {
                return 6;
            }
            if (isObject) {
                if (isAppend) {
                    // Implement by ArrayList
                    Field field;
                    try {
                        field = c.getDeclaredField(key);// get field for appending for not save the same object
                    } catch (NoSuchFieldException e) {
                        return 4;
                    }
                    field.setAccessible(true);
                    // test whether is the same
                    if ((FileUtil.getSingleton().searchItemInObjectFile(path, name, c, key,
                            field.get(object)) == null)) {
                        @SuppressWarnings("unchecked")
                        ArrayList<T> data = (ArrayList<T>) FileUtil.getSingleton().readFile(path, name, c, true, false);
                        if (data == null) { // not consider whether is new or error,all do clear
                            data = new ArrayList<T>();
                        }
                        data.add((T) object);
                        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
                        oos.writeObject(data);
                        oos.close();
                        return 1;
                    } else {
                        return 3;
                    }
                } else {
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
                    ArrayList<T> al = new ArrayList<>();
                    al.add((T) object);
                    oos.writeObject(al);
                    oos.close();
                    return 1;
                }
            } else {
                OutputStream os;
                if (isAppend) {// not to consider the same content.
                    os = new FileOutputStream(f, true);
                } else {
                    os = new FileOutputStream(f);
                }
                if (object instanceof byte[]) {
                    os.write((byte[]) object);
                    os.close();
                    return 1;
                } else if (object instanceof String) {
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    String content = (String) object;
                    bw.write(content, 0, content.length());
                    bw.close();
                    os.close();
                    return 1;
                } else {
                    os.close();
                    return 2;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 读文件的方法，由于返回的是Object，你需要进行强制转换。
     * <p>
     * 参数：
     * <p>
     * String:所需文件上一级的绝对路径。
     * <p>
     * String:所需文件名。
     * <p>
     * Class T :读取对象且输入path和name都为空时，方法会读取这个类的对象中的保存路径。不需要请输入null。
     * <p>
     * Boolean:是否以对象的形式读进来。
     * <p>
     * 上一项若为false，则只能读取byte[]或String的文件。
     * <p>
     * Boolean:是否为文本格式。
     * <p>
     * 在不以对象读取时，确认是不是文本形式的读入。
     *
     * @param
     * @return 若isObject为true:Object，你需要强制转换为ArrayList T 类型。
     * <p>
     * 若isObject为false,isString为true:String。
     * <p>
     * 若isObject为false,isString为false:byte[]。
     * <p>
     * 若出现错误:null。
     */
    @SuppressWarnings("unchecked")
    public synchronized <T> Object readFile(String path, String name, Class<T> objClass, Boolean isObject,
                                            Boolean isString) {
        try {
            File f = fileEmptyDeal(path, name, objClass);
            if (f == null) {
                return null;
            }
            if (isObject) {
                if (f.length() <= 0) {
                    return null;
                } else {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
                    ArrayList<T> al = new ArrayList<>();
                    try {// 这个地方
                        al = (ArrayList<T>) ois.readObject();
                    } catch (ClassCastException err) {
                        System.out.println(err);
                        ois.close();
                    }
                    ois.close();
                    return al;
                }
            } else {
                if (isString) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
                    StringBuffer sb = new StringBuffer();
                    int buff;
                    while ((buff = br.read()) != -1) {
                        sb.append((char) buff);
                    }
                    br.close();
                    return sb.toString();
                } else {
                    InputStream is = new FileInputStream(f);
                    int buff;
                    byte[] ans = new byte[is.available()];
                    int index = 0;
                    while ((buff = is.read()) != -1) {
                        ans[index] = (byte) buff;
                        index++;
                    }
                    is.close();
                    return ans;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 删除文件中对象的方法。注意，只能删除以对象类保存的文件。 参数： String:所需文件上一级的绝对路径。
     * <p>
     * String:所需文件名。
     * <p>
     * Class T:提供一个空的对象以获取其中的path和name
     * <p>
     * String:检查对象的属性名称。
     * <p>
     * Object:删除属性值为这个对象的内容。
     *
     * @param
     * @return <p>
     * -1:文件为空，无法加载。
     * <p>
     * 0:重大的错误。
     * <p>
     * 1:保存成功。
     * <p>
     * 2:找不到想要删除的对象。
     * <p>
     * 3:没有提供修改的属性。
     * <p>
     * 4:path||name没有给定且对象中不存在文件路径。
     */
    public synchronized <T> Integer delItemInObjectFile(String path, String name, Class<T> objClass, String key,
                                                        Object value) {
        try {
            File f = fileEmptyDeal(path, name, objClass);
            if (f == null) {
                return 4;
            }
            if (f.length() <= 0) {
                return -1;
            } else {
                @SuppressWarnings("unchecked")
                ArrayList<T> data = (ArrayList<T>) FileUtil.getSingleton().readFile(path, name, objClass, true, false);
                if (data == null) {
                    return -1;
                }
                boolean flag = false;
                int point = 0;
                for (int i = 0; i < data.size(); i++) {
                    Class<?> c = data.get(i).getClass();
                    Field field;
                    try {
                        field = c.getDeclaredField(key);
                    } catch (NoSuchFieldException e) {// no provided field
                        return 3;
                    }
                    field.setAccessible(true);
                    if (field.get(data.get(i)).equals(value)) {
                        point = i;
                        flag = true;
                        break;// only delete one
                    }
                }
                if (flag) {// save
                    data.remove(point);
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
                    oos.writeObject(data);
                    oos.close();
                    return 1;
                } else {
                    return 2;
                }
            }
        } catch (Exception e) {
            return 0;
        }
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 这个方法是去修改以对象形式存储的数据。
     * <p>
     * 参数：
     * <p>
     * String:所需文件上一级的绝对路径。
     * <p>
     * String:文件名。
     * <p>
     * String:根据这个属性名来查找。
     * <p>
     * Object:查找这个值。
     * <p>
     * T:替换新的对象。
     * <p>
     *
     * @param
     * @return <p>
     * -1:文件为空，无法加载。
     * <p>
     * 0:重大的错误。
     * <p>
     * 1:保存成功。
     * <p>
     * 2:没有找到想要修改的对象。
     * <p>
     * 3:没有提供修改的属性。
     * <p>
     * 4:path||name没有给定且对象中不存在文件路径。
     */
    public synchronized <T> Integer modifyItemInObjectFile(String path, String name, String key, Object value,
                                                           T newObject) {
        try {
            File f = fileEmptyDeal(path, name, newObject.getClass());
            if (f == null) {
                return 4;
            }
            if (f.length() <= 0) {
                return -1;
            } else {
                @SuppressWarnings("unchecked")
                ArrayList<T> data = (ArrayList<T>) FileUtil.getSingleton().readFile(path, name, newObject.getClass(),
                        true, false);
                if (data == null) {
                    return -1;
                }
                boolean flag = false;
                for (int i = 0; i < data.size(); i++) {
                    Class<?> c = data.get(i).getClass();
                    Field field;
                    try {
                        field = c.getDeclaredField(key);
                    } catch (NoSuchFieldException e) {// no provided field
                        return 3;
                    }
                    field.setAccessible(true);
                    if (field.get(data.get(i)).equals(value)) {
                        data.set(i, (T) newObject);
                        flag = true;
                        break;// only search one
                    }
                }
                if (flag) {// save
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
                    oos.writeObject(data);
                    oos.close();
                    return 1;
                } else {
                    return 2;
                }
            }
        } catch (Exception e) {
            return 0;
        }
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 查询文件中对象的方法
     * <p>
     * 参数：
     * <p>
     * String:所需文件上一级的绝对路径。
     * <p>
     * String:所需文件名。
     * <p>
     * Class T :提供一个空的对象以获取其中的path和name
     * <p>
     * String:根据这个属性名来查找。
     * <p>
     * Object:查找这个值。
     *
     * @param path
     * @return null找不到、文件为空、没有查询关键字的属性、异常。
     * <p>
     * T:文件中找到的对象。
     */
    public synchronized <T> T searchItemInObjectFile(String path, String name, Class<T> objClass, String key,
                                                     Object value) {
        try {
            File f = fileEmptyDeal(path, name, objClass);
            if (f == null) {
                return null;
            }
            if (f.length() <= 0) {
                return null;
            } else {
                @SuppressWarnings("unchecked")
                ArrayList<T> data = (ArrayList<T>) FileUtil.getSingleton().readFile(path, name, objClass, true, false);
                if (data == null) {
                    return null;
                }
                for (int i = 0; i < data.size(); i++) {
                    Class<?> c = data.get(i).getClass();
                    Field field;
                    try {
                        field = c.getDeclaredField(key);
                    } catch (NoSuchFieldException e) {// no provided field
                        return null;
                    }
                    field.setAccessible(true);
                    if (field.get(data.get(i)).equals(value)) {
                        return data.get(i);
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 其他Object的文件空检测
     *
     * @param
     * @return <p>
     * 成功返回File，失败返回Null
     **/
    private synchronized <T> File fileEmptyDeal(String absolutePathName) {
        try {
            File f = new File(absolutePathName);
            if (!f.exists()) {
                File fTemp = new File(f.getParent());
                if (!fTemp.exists()) {
                    fTemp.mkdirs();// create folder
                }
                f.createNewFile();// create file
            }
            return f;
        } catch (Exception err) {
            err.printStackTrace();
        }
        return null;
    }

    /**
     * 其他Object的保存方式
     *
     * @param
     * @return <p>
     * -1:文件不存在
     * <p>
     * 0:保存失败
     * <p>
     * 1:保存成功
     */
    public synchronized <T> int saveFile(String absolutePathName, T object) {
        ObjectOutputStream oos = null;
        try {
            File f = fileEmptyDeal(absolutePathName);
            if (f == null) {
                return -1;
            }
            oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(object);
            oos.close();
            return 1;
        } catch (Exception err) {
            err.printStackTrace();
            try {
                oos.close();
            } catch (Exception e) {
            }
        }
        return 0;
    }

    /**
     * 其他Object的读取方式
     *
     * @param
     * @return <p>
     * 成功读取返回T，失败返回null
     * <p>
     */
    @SuppressWarnings("unchecked")
    public synchronized <T> T readFile(String absolutePathName) {
        T ans = null;
        ObjectInputStream ois = null;
        try {
            File f = fileEmptyDeal(absolutePathName);
            ois = new ObjectInputStream(new FileInputStream(f));
            ans = (T) ois.readObject();
            ois.close();
            return ans;
        } catch (Exception err) {
            System.err.println("FileUtil: " + err.getMessage());
            try {
                ois.close();
            } catch (Exception e) {
            }
            return null;
        }
    }
}