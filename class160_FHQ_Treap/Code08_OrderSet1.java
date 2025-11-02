package class152;

// FHQ-Treap实现Order Statistic Set
// SPOJ ORDERSET - Order statistic set
// 实现有序集合，支持插入、删除、查询第k小数、查询某数的排名等操作
// 题目链接: https://www.spoj.com/problems/ORDERSET/
// 题目描述: 维护一个动态集合，支持插入、删除、查询第k小数、查询某数的排名等操作
// 操作类型:
// I x : 插入元素x
// D x : 删除元素x
// K x : 查询第x小的元素
// C x : 查询元素x的排名（比x小的数的个数）

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code08_OrderSet1 {
    
    // 最大节点数
    public static int MAXN = 200001;
    
    // 整棵树的头节点编号
    public static int head = 0;
    
    // 空间使用计数
    public static int cnt = 0;
    
    // 节点的key值
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
        if (x < 1 || x > size[head]) {
            return Integer.MAX_VALUE; // 表示不存在
        }
        return index(head, x);
    }
    
    // 查询数值num的前驱
    public static int pre(int i, int num) {
        if (i == 0) {
            return Integer.MIN_VALUE;
        }
        if (key[i] >= num) {
            return pre(left[i], num);
        } else {
            return Math.max(key[i], pre(right[i], num));
        }
    }
    
    // 查询数值num的前驱
    public static int preByValue(int num) {
        return pre(head, num);
    }
    
    // 查询数值num的后继
    public static int post(int i, int num) {
        if (i == 0) {
            return Integer.MAX_VALUE;
        }
        if (key[i] <= num) {
            return post(right[i], num);
        } else {
            return Math.min(key[i], post(left[i], num));
        }
    }
    
    // 查询数值num的后继
    public static int postByValue(int num) {
        return post(head, num);
    }
    
    public static void main(String[] args) throws IOException {
        init();
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        in.nextToken();
        int q = (int) in.nval; // 操作次数
        
        for (int i = 0; i < q; i++) {
            String operation = br.readLine().trim();
            String[] parts = operation.split(" ");
            
            switch (parts[0]) {
                case "I": // 插入
                    insert(Integer.parseInt(parts[1]));
                    break;
                case "D": // 删除
                    remove(Integer.parseInt(parts[1]));
                    break;
                case "K": // 查询第k小
                    int k = Integer.parseInt(parts[1]);
                    int result = indexByRank(k);
                    if (result == Integer.MAX_VALUE) {
                        out.println("invalid");
                    } else {
                        out.println(result);
                    }
                    break;
                case "C": // 查询排名
                    out.println(small(head, Integer.parseInt(parts[1])));
                    break;
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}