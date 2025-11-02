package class152;

// FHQ-Treap实现Feed the dogs
// POJ 2761 Feed the dogs
// 实现查询区间第k小元素
// 测试链接 : http://poj.org/problem?id=2761

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code12_FeedTheDogs1 {
    
    // 最大节点数
    public static int MAXN = 100001;
    
    // 整棵树的头节点编号
    public static int head = 0;
    
    // 空间使用计数
    public static int cnt = 0;
    
    // 节点的key值（元素值）
    public static int[] key = new int[MAXN];
    
    // 节点key的计数
    public static int[] count = new int[MAXN];
    
    // 左孩子
    public static int[] left = new int[MAXN];
    
    // 右孩子
    public static int[] right = new int[MAXN];
    
    // 数字总数
    public static int[] size = new int[MAXN];
    
    // 节点优先级
    public static double[] priority = new double[MAXN];
    
    // 初始化
    public static void init() {
        head = 0;
        cnt = 0;
        Arrays.fill(key, 0);
        Arrays.fill(count, 0);
        Arrays.fill(left, 0);
        Arrays.fill(right, 0);
        Arrays.fill(size, 0);
        Arrays.fill(priority, 0.0);
    }
    
    // 更新节点信息
    public static void up(int i) {
        size[i] = size[left[i]] + size[right[i]] + count[i];
    }
    
    // 按值分裂，将树i按照数值num分裂为两棵树
    public static void split(int l, int r, int i, int num) {
        if (i == 0) {
            right[l] = left[r] = 0;
        } else {
            if (key[i] <= num) {
                right[l] = i;
                split(i, r, right[i], num);
            } else {
                left[r] = i;
                split(l, i, left[i], num);
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
    
    // 查找值为num的节点
    public static int find(int i, int num) {
        if (i == 0) {
            return 0;
        }
        if (key[i] == num) {
            return i;
        } else if (key[i] > num) {
            return find(left[i], num);
        } else {
            return find(right[i], num);
        }
    }
    
    // 改变节点计数
    public static void changeCount(int i, int num, int change) {
        if (key[i] == num) {
            count[i] += change;
        } else if (key[i] > num) {
            changeCount(left[i], num, change);
        } else {
            changeCount(right[i], num, change);
        }
        up(i);
    }
    
    // 插入数值
    public static void insert(int num) {
        if (find(head, num) != 0) {
            changeCount(head, num, 1);
        } else {
            split(0, 0, head, num);
            cnt++;
            key[cnt] = num;
            count[cnt] = size[cnt] = 1;
            priority[cnt] = Math.random();
            head = merge(merge(right[0], cnt), left[0]);
        }
    }
    
    // 删除数值
    public static void remove(int num) {
        int i = find(head, num);
        if (i != 0) {
            if (count[i] > 1) {
                changeCount(head, num, -1);
            } else {
                split(0, 0, head, num);
                int lm = right[0];
                int r = left[0];
                split(0, 0, lm, num - 1);
                int l = right[0];
                head = merge(l, r);
            }
        }
    }
    
    // 计算小于num的数的个数
    public static int small(int i, int num) {
        if (i == 0) {
            return 0;
        }
        if (key[i] >= num) {
            return small(left[i], num);
        } else {
            return size[left[i]] + count[i] + small(right[i], num);
        }
    }
    
    // 查询数值num的排名
    public static int rank(int num) {
        return small(head, num) + 1;
    }
    
    // 查询排名为x的数值
    public static int index(int i, int x) {
        if (size[left[i]] >= x) {
            return index(left[i], x);
        } else if (size[left[i]] + count[i] < x) {
            return index(right[i], x - size[left[i]] - count[i]);
        }
        return key[i];
    }
    
    // 查询排名为x的数值
    public static int indexByRank(int x) {
        return index(head, x);
    }
    
    public static void main(String[] args) throws IOException {
        init();
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        in.nextToken();
        int n = (int) in.nval; // 序列长度
        
        // 读取序列
        int[] arr = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            arr[i] = (int) in.nval;
        }
        
        in.nextToken();
        int m = (int) in.nval; // 查询次数
        
        // 处理查询
        for (int i = 0; i < m; i++) {
            in.nextToken();
            int l = (int) in.nval; // 区间左端点
            in.nextToken();
            int r = (int) in.nval; // 区间右端点
            in.nextToken();
            int k = (int) in.nval; // 查询第k小
            
            // 重新初始化FHQ Treap
            init();
            
            // 将区间[l, r]的元素插入到FHQ Treap中
            for (int j = l; j <= r; j++) {
                insert(arr[j]);
            }
            
            // 查询第k小
            out.println(indexByRank(k));
        }
        
        out.flush();
        out.close();
        br.close();
    }
}