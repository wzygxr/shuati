package class152;

// FHQ-Treap实现To the moon
// SPOJ TTM - To the moon
// 实现可持久化数组操作
// 测试链接 : https://www.spoj.com/problems/TTM/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code11_ToTheMoon1 {
    
    // 最大节点数
    public static int MAXN = 100001;
    
    // 最大版本数
    public static int MAXV = 100001;
    
    // 空间使用计数
    public static int cnt = 0;
    
    // 节点的key值（数组元素值）
    public static int[] key = new int[MAXN];
    
    // 节点的加法标记
    public static int[] add = new int[MAXN];
    
    // 左孩子
    public static int[] left = new int[MAXN];
    
    // 右孩子
    public static int[] right = new int[MAXN];
    
    // 子树大小
    public static int[] size = new int[MAXN];
    
    // 节点优先级
    public static double[] priority = new double[MAXN];
    
    // 版本数组，存储每个版本的根节点
    public static int[] version = new int[MAXV];
    
    // 当前版本号
    public static int currentVersion = 0;
    
    // 初始化
    public static void init() {
        cnt = 0;
        currentVersion = 0;
        Arrays.fill(key, 0);
        Arrays.fill(add, 0);
        Arrays.fill(left, 0);
        Arrays.fill(right, 0);
        Arrays.fill(size, 0);
        Arrays.fill(priority, 0.0);
        Arrays.fill(version, 0);
    }
    
    // 更新节点信息
    public static void up(int i) {
        size[i] = size[left[i]] + size[right[i]] + 1;
    }
    
    // 下传标记
    public static void down(int i) {
        if (add[i] != 0) {
            // 创建新节点以实现可持久化
            if (left[i] != 0) {
                cnt++;
                key[cnt] = key[left[i]];
                add[cnt] = add[left[i]];
                left[cnt] = left[left[i]];
                right[cnt] = right[left[i]];
                size[cnt] = size[left[i]];
                priority[cnt] = priority[left[i]];
                key[cnt] += add[i];
                add[cnt] += add[i];
                left[i] = cnt;
            }
            if (right[i] != 0) {
                cnt++;
                key[cnt] = key[right[i]];
                add[cnt] = add[right[i]];
                left[cnt] = left[right[i]];
                right[cnt] = right[right[i]];
                size[cnt] = size[right[i]];
                priority[cnt] = priority[right[i]];
                key[cnt] += add[i];
                add[cnt] += add[i];
                right[i] = cnt;
            }
            add[i] = 0;
        }
    }
    
    // 按位置分裂，将树i按照位置pos分裂为两棵树
    public static void splitByPosition(int l, int r, int i, int pos) {
        if (i == 0) {
            right[l] = left[r] = 0;
        } else {
            down(i);
            if (size[left[i]] + 1 <= pos) {
                right[l] = i;
                splitByPosition(i, r, right[i], pos - size[left[i]] - 1);
            } else {
                left[r] = i;
                splitByPosition(l, i, left[i], pos);
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
            down(l);
            right[l] = merge(right[l], r);
            up(l);
            return l;
        } else {
            down(r);
            left[r] = merge(l, left[r]);
            up(r);
            return r;
        }
    }
    
    // 构建初始数组
    public static int build(int l, int r, int[] arr) {
        if (l > r) {
            return 0;
        }
        int mid = (l + r) / 2;
        cnt++;
        int root = cnt;
        key[root] = arr[mid];
        size[root] = 1;
        priority[root] = Math.random();
        
        if (l == r) {
            return root;
        }
        
        left[root] = build(l, mid - 1, arr);
        right[root] = build(mid + 1, r, arr);
        up(root);
        return root;
    }
    
    // 区间加法（可持久化）
    public static int addRange(int root, int l, int r, int value) {
        splitByPosition(0, 0, root, l - 1);
        int leftTree = right[0];
        splitByPosition(0, 0, leftTree, r - l + 1);
        int middleTree = right[0];
        
        // 创建新节点以实现可持久化
        cnt++;
        key[cnt] = key[middleTree];
        add[cnt] = add[middleTree];
        left[cnt] = left[middleTree];
        right[cnt] = right[middleTree];
        size[cnt] = size[middleTree];
        priority[cnt] = priority[middleTree];
        key[cnt] += value;
        add[cnt] += value;
        
        // 重新合并
        return merge(merge(left[0], cnt), right[0]);
    }
    
    // 查询指定位置的值
    public static int query(int root, int pos) {
        splitByPosition(0, 0, root, pos - 1);
        int leftTree = right[0];
        splitByPosition(0, 0, leftTree, 1);
        int middleTree = right[0];
        
        int result = key[middleTree];
        
        // 重新合并
        merge(merge(left[0], middleTree), right[0]);
        
        return result;
    }
    
    // 获取树中第pos个节点的key值
    public static int getKth(int i, int pos) {
        if (i == 0) {
            return 0;
        }
        down(i);
        if (size[left[i]] + 1 == pos) {
            return key[i];
        } else if (size[left[i]] + 1 > pos) {
            return getKth(left[i], pos);
        } else {
            return getKth(right[i], pos - size[left[i]] - 1);
        }
    }
    
    // 获取第pos个元素
    public static int getKthElement(int root, int pos) {
        return getKth(root, pos);
    }
    
    public static void main(String[] args) throws IOException {
        init();
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        in.nextToken();
        int n = (int) in.nval; // 数组长度
        in.nextToken();
        int m = (int) in.nval; // 操作次数
        
        // 读取初始数组
        int[] arr = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            arr[i] = (int) in.nval;
        }
        
        // 构建初始版本
        version[currentVersion] = build(1, n, arr);
        
        // 处理操作
        for (int i = 0; i < m; i++) {
            String operation = br.readLine().trim();
            String[] parts = operation.split(" ");
            
            if (parts[0].equals("Q")) {
                // 查询操作
                int versionId = Integer.parseInt(parts[1]);
                int pos = Integer.parseInt(parts[2]);
                out.println(getKthElement(version[versionId], pos));
            } else if (parts[0].equals("C")) {
                // 修改操作
                int versionId = Integer.parseInt(parts[1]);
                int l = Integer.parseInt(parts[2]);
                int r = Integer.parseInt(parts[3]);
                int value = Integer.parseInt(parts[4]);
                currentVersion++;
                version[currentVersion] = addRange(version[versionId], l, r, value);
            } else {
                // 回到历史版本
                int versionId = Integer.parseInt(parts[1]);
                currentVersion = versionId;
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}