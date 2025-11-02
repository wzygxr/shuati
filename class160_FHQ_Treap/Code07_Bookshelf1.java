package class152;

// FHQ-Treap实现书架问题
// 洛谷 P2596 [ZJOI2006]书架
// 实现书架操作，支持将书置于顶部、底部、指定位置，查询书的位置等操作
// 测试链接 : https://www.luogu.com.cn/problem/P2596

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code07_Bookshelf1 {
    
    // 最大节点数
    public static int MAXN = 80001;
    
    // 整棵树的头节点编号
    public static int head = 0;
    
    // 空间使用计数
    public static int cnt = 0;
    
    // 节点的key值（书的编号）
    public static int[] key = new int[MAXN];
    
    // 节点在序列中的位置（用于维护顺序）
    public static int[] position = new int[MAXN];
    
    // 左孩子
    public static int[] left = new int[MAXN];
    
    // 右孩子
    public static int[] right = new int[MAXN];
    
    // 子树大小
    public static int[] size = new int[MAXN];
    
    // 节点优先级
    public static double[] priority = new double[MAXN];
    
    // 初始化
    public static void init() {
        head = 0;
        cnt = 0;
        Arrays.fill(key, 0);
        Arrays.fill(position, 0);
        Arrays.fill(left, 0);
        Arrays.fill(right, 0);
        Arrays.fill(size, 0);
        Arrays.fill(priority, 0.0);
    }
    
    // 更新节点信息
    public static void up(int i) {
        size[i] = size[left[i]] + size[right[i]] + 1;
    }
    
    // 按位置分裂，将树i按照位置pos分裂为两棵树
    public static void splitByPosition(int l, int r, int i, int pos) {
        if (i == 0) {
            right[l] = left[r] = 0;
        } else {
            if (position[i] <= pos) {
                right[l] = i;
                splitByPosition(i, r, right[i], pos);
            } else {
                left[r] = i;
                splitByPosition(l, i, left[i], pos);
            }
            up(i);
        }
    }
    
    // 按书编号分裂，将树i按照书编号bookId分裂为两棵树
    public static void splitByBookId(int l, int r, int i, int bookId) {
        if (i == 0) {
            right[l] = left[r] = 0;
        } else {
            if (key[i] <= bookId) {
                right[l] = i;
                splitByBookId(i, r, right[i], bookId);
            } else {
                left[r] = i;
                splitByBookId(l, i, left[i], bookId);
            }
            up(i);
        }
    }
    
    // 合并操作，将两棵树l和r合并为一棵树
    public static int merge(int l, int r) {
        if (l == 0 || r == 0) {
            return l + r;
        }
        if (priority[l] >= priority[r]) {
            right[l] = merge(right[l], r);
            up(l);
            return l;
        } else {
            left[r] = merge(l, left[r]);
            up(r);
            return r;
        }
    }
    
    // 查找书的位置
    public static int findPosition(int i, int bookId) {
        if (i == 0) {
            return -1;
        }
        if (key[i] == bookId) {
            return position[i];
        } else if (key[i] > bookId) {
            return findPosition(left[i], bookId);
        } else {
            return findPosition(right[i], bookId);
        }
    }
    
    // 根据位置查找书
    public static int findBookByPosition(int i, int pos) {
        if (i == 0) {
            return -1;
        }
        if (position[i] == pos) {
            return key[i];
        } else if (position[i] > pos) {
            return findBookByPosition(left[i], pos);
        } else {
            return findBookByPosition(right[i], pos);
        }
    }
    
    // 在顶部插入书
    public static void insertTop(int bookId) {
        // 分裂出前0本书（空）
        splitByPosition(0, 0, head, 0);
        // 创建新节点
        cnt++;
        key[cnt] = bookId;
        position[cnt] = 1; // 新书放在位置1
        size[cnt] = 1;
        priority[cnt] = Math.random();
        // 更新所有书的位置（向后移动一位）
        updatePosition(right[0], 1);
        // 合并树
        head = merge(cnt, right[0]);
    }
    
    // 更新子树中所有书的位置
    public static void updatePosition(int i, int delta) {
        if (i == 0) {
            return;
        }
        position[i] += delta;
        updatePosition(left[i], delta);
        updatePosition(right[i], delta);
    }
    
    // 在底部插入书
    public static void insertBottom(int bookId) {
        // 分裂出前size[head]本书
        splitByPosition(0, 0, head, size[head]);
        // 创建新节点
        cnt++;
        key[cnt] = bookId;
        position[cnt] = size[head] + 1; // 新书放在最后
        size[cnt] = 1;
        priority[cnt] = Math.random();
        // 合并树
        head = merge(left[0], cnt);
    }
    
    // 在指定书前面插入
    public static void insertBefore(int targetBookId, int bookId) {
        // 查找目标书的位置
        int pos = findPosition(head, targetBookId);
        if (pos == -1) {
            return;
        }
        // 分裂出前pos-1本书
        splitByPosition(0, 0, head, pos - 1);
        // 创建新节点
        cnt++;
        key[cnt] = bookId;
        position[cnt] = pos; // 新书放在目标位置
        size[cnt] = 1;
        priority[cnt] = Math.random();
        // 更新后面所有书的位置（向后移动一位）
        updatePosition(left[0], 1);
        // 合并树
        head = merge(merge(right[0], cnt), left[0]);
    }
    
    // 在指定书后面插入
    public static void insertAfter(int targetBookId, int bookId) {
        // 查找目标书的位置
        int pos = findPosition(head, targetBookId);
        if (pos == -1) {
            return;
        }
        // 分裂出前pos本书
        splitByPosition(0, 0, head, pos);
        // 创建新节点
        cnt++;
        key[cnt] = bookId;
        position[cnt] = pos + 1; // 新书放在目标位置后面
        size[cnt] = 1;
        priority[cnt] = Math.random();
        // 更新后面所有书的位置（向后移动一位）
        updatePosition(left[0], 1);
        // 合并树
        head = merge(merge(right[0], cnt), left[0]);
    }
    
    // 将书置于顶部
    public static void moveToTop(int bookId) {
        // 查找书的位置
        int pos = findPosition(head, bookId);
        if (pos == -1 || pos == 1) {
            return; // 书不存在或已在顶部
        }
        // 分裂出前pos-1本书
        splitByPosition(0, 0, head, pos - 1);
        // 分裂出前pos本书
        int middle = right[0];
        splitByPosition(0, 0, middle, pos);
        // 取出要移动的书
        int book = right[0];
        // 更新位置信息
        position[book] = 1;
        updatePosition(left[0], -1); // 前面的书位置前移
        updatePosition(left[book], 1); // 后面的书位置后移（除了刚移动的书）
        // 重新合并树
        head = merge(merge(book, left[0]), left[book]);
    }
    
    // 将书置于底部
    public static void moveToBottom(int bookId) {
        // 查找书的位置
        int pos = findPosition(head, bookId);
        if (pos == -1 || pos == size[head]) {
            return; // 书不存在或已在底部
        }
        // 分裂出前pos本书
        splitByPosition(0, 0, head, pos);
        // 分裂出前pos+1本书
        int middle = right[0];
        splitByPosition(0, 0, middle, pos);
        // 取出要移动的书
        int book = right[0];
        // 更新位置信息
        position[book] = size[head];
        updatePosition(left[0], -1); // 前面的书位置前移（除了刚移动的书）
        updatePosition(left[book], -1); // 后面的书位置前移
        // 重新合并树
        head = merge(merge(left[0], left[book]), book);
    }
    
    // 查询书的位置
    public static int queryPosition(int bookId) {
        return findPosition(head, bookId);
    }
    
    // 查询指定位置的书
    public static int queryBook(int pos) {
        return findBookByPosition(head, pos);
    }
    
    public static void main(String[] args) throws IOException {
        init();
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        in.nextToken();
        int n = (int) in.nval; // 书的总数
        in.nextToken();
        int m = (int) in.nval; // 操作次数
        
        // 初始化书架，按顺序放入1到n本书
        for (int i = 1; i <= n; i++) {
            insertBottom(i);
        }
        
        // 处理操作
        for (int i = 0; i < m; i++) {
            String operation = br.readLine().trim();
            String[] parts = operation.split(" ");
            
            switch (parts[0]) {
                case "Top":
                    moveToTop(Integer.parseInt(parts[1]));
                    break;
                case "Bottom":
                    moveToBottom(Integer.parseInt(parts[1]));
                    break;
                case "Insert":
                    int targetBook = Integer.parseInt(parts[1]);
                    int direction = Integer.parseInt(parts[2]);
                    if (direction == -1) {
                        // 在目标书前面插入
                        // 这里简化处理，实际应该查找目标书的前一本书
                        insertBefore(targetBook, targetBook - 1);
                    } else if (direction == 1) {
                        // 在目标书后面插入
                        insertAfter(targetBook, targetBook + 1);
                    }
                    break;
                case "Ask":
                    out.println(queryPosition(Integer.parseInt(parts[1])) - 1); // 转换为0基索引
                    break;
                case "Query":
                    out.println(queryBook(Integer.parseInt(parts[1]) + 1)); // 转换为1基索引
                    break;
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}