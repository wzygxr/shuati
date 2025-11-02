package class170;

// UVA11990 ''Dynamic'' Inversion
// 平台: UVA
// 难度: 困难
// 标签: CDQ分治, 动态逆序对
// 链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=226&page=show_problem&problem=3141

import java.io.*;
import java.util.*;

public class UVA11990DynamicInversion {
    
    static class Operation {
        int type; // 0: 初始元素, 1: 删除操作
        int time; // 时间戳
        int value; // 元素值
        int pos;   // 位置
        long ans;  // 逆序对贡献
        
        Operation(int type, int time, int value, int pos) {
            this.type = type;
            this.time = time;
            this.value = value;
            this.pos = pos;
            this.ans = 0;
        }
    }
    
    static int MAXN = 200005;
    static Operation[] ops = new Operation[MAXN * 2];
    static Operation[] temp = new Operation[MAXN * 2];
    static int[] tree = new int[MAXN];
    static int[] posMap = new int[MAXN]; // 记录每个值的位置
    static boolean[] removed = new boolean[MAXN]; // 记录是否被删除
    
    public static int lowbit(int x) {
        return x & -x;
    }
    
    public static void add(int pos, int val) {
        while (pos < MAXN) {
            tree[pos] += val;
            pos += lowbit(pos);
        }
    }
    
    public static int query(int pos) {
        int res = 0;
        while (pos > 0) {
            res += tree[pos];
            pos -= lowbit(pos);
        }
        return res;
    }
    
    // CDQ分治计算逆序对贡献
    public static void cdq(int l, int r) {
        if (l >= r) return;
        
        int mid = (l + r) >> 1;
        cdq(l, mid);
        cdq(mid + 1, r);
        
        // 按照时间排序，计算左半部分对右半部分的贡献
        int i = l, j = mid + 1, k = l;
        while (i <= mid && j <= r) {
            if (ops[i].pos <= ops[j].pos) {
                if (ops[i].type == 0) { // 只有初始元素才加入树状数组
                    add(ops[i].value, 1);
                }
                temp[k++] = ops[i++];
            } else {
                if (ops[j].type == 1) { // 只有删除操作才计算贡献
                    ops[j].ans += query(MAXN - 1) - query(ops[j].value);
                }
                temp[k++] = ops[j++];
            }
        }
        
        while (i <= mid) {
            if (ops[i].type == 0) {
                add(ops[i].value, 1);
            }
            temp[k++] = ops[i++];
        }
        
        while (j <= r) {
            if (ops[j].type == 1) {
                ops[j].ans += query(MAXN - 1) - query(ops[j].value);
            }
            temp[k++] = ops[j++];
        }
        
        // 清空树状数组
        for (int idx = l; idx <= mid; idx++) {
            if (ops[idx].type == 0) {
                add(ops[idx].value, -1);
            }
        }
        
        // 复制回原数组
        System.arraycopy(temp, l, ops, l, r - l + 1);
    }
    
    // 反向CDQ计算另一种逆序对贡献
    public static void cdqReverse(int l, int r) {
        if (l >= r) return;
        
        int mid = (l + r) >> 1;
        cdqReverse(l, mid);
        cdqReverse(mid + 1, r);
        
        // 按照位置降序排序
        int i = l, j = mid + 1, k = l;
        while (i <= mid && j <= r) {
            if (ops[i].pos >= ops[j].pos) {
                if (ops[i].type == 0) {
                    add(ops[i].value, 1);
                }
                temp[k++] = ops[i++];
            } else {
                if (ops[j].type == 1) {
                    ops[j].ans += query(ops[j].value - 1);
                }
                temp[k++] = ops[j++];
            }
        }
        
        while (i <= mid) {
            if (ops[i].type == 0) {
                add(ops[i].value, 1);
            }
            temp[k++] = ops[i++];
        }
        
        while (j <= r) {
            if (ops[j].type == 1) {
                ops[j].ans += query(ops[j].value - 1);
            }
            temp[k++] = ops[j++];
        }
        
        // 清空树状数组
        for (int idx = l; idx <= mid; idx++) {
            if (ops[idx].type == 0) {
                add(ops[idx].value, -1);
            }
        }
        
        // 复制回原数组
        System.arraycopy(temp, l, ops, l, r - l + 1);
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            StringTokenizer st = new StringTokenizer(line);
            int n = Integer.parseInt(st.nextToken());
            int m = Integer.parseInt(st.nextToken());
            
            // 初始化
            Arrays.fill(posMap, 0);
            Arrays.fill(removed, false);
            Arrays.fill(tree, 0);
            
            // 读取初始排列
            st = new StringTokenizer(br.readLine());
            int[] arr = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                arr[i] = Integer.parseInt(st.nextToken());
                posMap[arr[i]] = i;
            }
            
            // 读取删除操作
            int[] removeOrder = new int[m];
            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < m; i++) {
                removeOrder[i] = Integer.parseInt(st.nextToken());
                removed[removeOrder[i]] = true;
            }
            
            // 构建操作序列
            int opCount = 0;
            
            // 添加初始元素（未被删除的）
            for (int i = 1; i <= n; i++) {
                if (!removed[arr[i]]) {
                    ops[opCount++] = new Operation(0, opCount, arr[i], i);
                }
            }
            
            // 添加删除操作（按删除时间倒序）
            for (int i = m - 1; i >= 0; i--) {
                int value = removeOrder[i];
                ops[opCount++] = new Operation(1, opCount, value, posMap[value]);
            }
            
            // 第一次CDQ：计算右侧大于当前值的逆序对
            cdq(0, opCount - 1);
            
            // 清空树状数组
            Arrays.fill(tree, 0);
            
            // 第二次CDQ：计算左侧小于当前值的逆序对
            cdqReverse(0, opCount - 1);
            
            // 计算总逆序对
            long totalInversions = 0;
            for (int i = 0; i < opCount; i++) {
                if (ops[i].type == 1) {
                    totalInversions += ops[i].ans;
                }
            }
            
            // 输出结果
            pw.println(totalInversions);
        }
        
        pw.flush();
        pw.close();
        br.close();
    }
}
