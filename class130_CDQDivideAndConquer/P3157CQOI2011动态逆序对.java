package class170;

// P3157 [CQOI2011]动态逆序对
// 平台: 洛谷
// 难度: 省选/NOI-
// 标签: CDQ分治, 动态逆序对
// 链接: https://www.luogu.com.cn/problem/P3157
// 
// 题目描述:
// 给定一个长度为n的排列，1~n所有数字都出现一次
// 如果，前面的数 > 后面的数，那么这两个数就组成一个逆序对
// 给定一个长度为m的数组，表示依次删除的数字
// 打印每次删除数字前，排列中一共有多少逆序对，一共m条打印
// 
// 示例:
// 输入:
// 5 4
// 1 5 3 4 2
// 3 5 4 2
// 
// 输出:
// 5
// 2
// 1
// 0
// 
// 解题思路:
// 使用CDQ分治解决动态逆序对问题。将问题转化为四维偏序问题：
// 1. 第一维：时间，表示删除操作的时间
// 2. 第二维：数值，表示元素的值
// 3. 第三维：位置，表示元素在原数组中的位置
// 4. 第四维：操作类型，用于区分插入和删除操作
// 
// 我们将每个元素看作两种操作：
// 1. 初始操作：在时间0时，所有元素都存在
// 2. 删除操作：在时间t时，删除某个元素
// 
// 对于每个删除操作，我们需要计算它对逆序对数量的影响：
// 1. 作为较大元素，统计在其位置之后、值更小的元素个数
// 2. 作为较小元素，统计在其位置之前、值更大的元素个数
// 
// 时间复杂度：O((n+m) log^2 (n+m))
// 空间复杂度：O(n+m)

import java.io.*;
import java.util.*;

class OperationP3157 implements Comparable<OperationP3157> {
    int time, value, position, type, id;
    
    public OperationP3157(int time, int value, int position, int type, int id) {
        this.time = time;
        this.value = value;
        this.position = position;
        this.type = type;  // 1表示初始元素，-1表示删除操作
        this.id = id;
    }
    
    @Override
    public int compareTo(OperationP3157 other) {
        if (this.position != other.position) {
            return Integer.compare(this.position, other.position);
        }
        return Integer.compare(this.type, other.type);
    }
}

public class P3157CQOI2011动态逆序对 {
    private static int[] bit;  // 树状数组
    
    // 树状数组操作
    private static int lowbit(int x) {
        return x & (-x);
    }
    
    private static void add(int x, int v, int n) {
        for (int i = x; i <= n; i += lowbit(i)) {
            bit[i] += v;
        }
    }
    
    private static int query(int x) {
        int res = 0;
        for (int i = x; i > 0; i -= lowbit(i)) {
            res += bit[i];
        }
        return res;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] nm = reader.readLine().split(" ");
        int n = Integer.parseInt(nm[0]);
        int m = Integer.parseInt(nm[1]);
        
        int[] nums = new int[n + 1];
        int[] pos = new int[n + 1];
        String[] numStr = reader.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            nums[i] = Integer.parseInt(numStr[i - 1]);
            pos[nums[i]] = i;
        }
        
        int[] del = new int[m + 1];
        String[] delStr = reader.readLine().split(" ");
        for (int i = 1; i <= m; i++) {
            del[i] = Integer.parseInt(delStr[i - 1]);
        }
        
        // 构造操作序列
        OperationP3157[] ops = new OperationP3157[n + m];
        long[] ans = new long[m + 1];
        
        int cnt = 0;
        // 初始操作
        for (int i = 1; i <= n; i++) {
            ops[cnt++] = new OperationP3157(0, nums[i], i, 1, 0);
        }
        
        // 删除操作
        for (int i = 1; i <= m; i++) {
            ops[cnt++] = new OperationP3157(i, del[i], pos[del[i]], -1, i);
        }
        
        bit = new int[n + 1];
        
        // 按值排序
        Arrays.sort(ops, (a, b) -> {
            if (a.value != b.value) {
                return Integer.compare(a.value, b.value);
            }
            return Integer.compare(b.type, a.type);  // 删除操作优先于插入操作
        });
        
        // 执行CDQ分治
        cdq(ops, ans, 0, cnt - 1, n);
        
        // 计算前缀和
        for (int i = 1; i <= m; i++) {
            ans[i] += ans[i - 1];
        }
        
        // 输出结果
        long total = ans[0];
        out.println(total);
        for (int i = 1; i < m; i++) {
            total -= ans[i];
            out.println(total);
        }
        
        out.flush();
        out.close();
    }
    
    // CDQ分治主函数
    private static void cdq(OperationP3157[] ops, long[] ans, int l, int r, int n) {
        if (l >= r) return;
        
        int mid = (l + r) >> 1;
        cdq(ops, ans, l, mid, n);
        cdq(ops, ans, mid + 1, r, n);
        
        // 合并过程，计算左半部分对右半部分的贡献
        OperationP3157[] tmp = new OperationP3157[r - l + 1];
        int i = l, j = mid + 1, k = 0;
        
        // 从左到右统计左侧值大的数量
        while (i <= mid && j <= r) {
            if (ops[i].position < ops[j].position) {
                if (ops[i].type == 1) {
                    add(ops[i].value, ops[i].type, n);
                }
                tmp[k++] = ops[i++];
            } else {
                if (ops[j].type == -1) {
                    ans[ops[j].id] += ops[j].type * (query(n) - query(ops[j].value));
                }
                tmp[k++] = ops[j++];
            }
        }
        
        // 处理剩余元素
        while (i <= mid) {
            tmp[k++] = ops[i++];
        }
        while (j <= r) {
            if (ops[j].type == -1) {
                ans[ops[j].id] += ops[j].type * (query(n) - query(ops[j].value));
            }
            tmp[k++] = ops[j++];
        }
        
        // 清除树状数组
        for (int t = l; t <= mid; t++) {
            if (ops[t].type == 1) {
                add(ops[t].value, -ops[t].type, n);
            }
        }
        
        // 从右到左统计右侧值小的数量
        i = mid;
        j = r;
        while (i >= l && j > mid) {
            if (ops[i].position > ops[j].position) {
                if (ops[i].type == 1) {
                    add(ops[i].value, ops[i].type, n);
                }
                tmp[--k] = ops[i--];
            } else {
                if (ops[j].type == -1) {
                    ans[ops[j].id] += ops[j].type * query(ops[j].value - 1);
                }
                tmp[--k] = ops[j--];
            }
        }
        
        // 处理剩余元素
        while (i >= l) {
            tmp[--k] = ops[i--];
        }
        while (j > mid) {
            if (ops[j].type == -1) {
                ans[ops[j].id] += ops[j].type * query(ops[j].value - 1);
            }
            tmp[--k] = ops[j--];
        }
        
        // 清除树状数组
        for (int t = l; t <= mid; t++) {
            if (ops[t].type == 1) {
                add(ops[t].value, -ops[t].type, n);
            }
        }
        
        // 按位置排序
        Arrays.sort(tmp, l, r + 1);
        
        // 将临时数组内容复制回原数组
        for (int t = 0; t < tmp.length; t++) {
            ops[l + t] = tmp[t];
        }
    }
}