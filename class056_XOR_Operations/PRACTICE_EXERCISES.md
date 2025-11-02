# 异或运算练习题集

## 一、基础练习题

### 1.1 位操作基础
**题目1**: 实现不使用临时变量交换两个整数
```java
// 要求：使用异或运算实现
public void swap(int a, int b) {
    a = a ^ b;
    b = a ^ b;
    a = a ^ b;
}
```

**题目2**: 判断一个数是否是2的幂
```java
public boolean isPowerOfTwo(int n) {
    return n > 0 && (n & (n - 1)) == 0;
}
```

**题目3**: 计算一个数的二进制表示中1的个数
```java
public int countOnes(int n) {
    int count = 0;
    while (n != 0) {
        n = n & (n - 1);
        count++;
    }
    return count;
}
```

### 1.2 简单应用
**题目4**: 找出数组中唯一出现一次的数字
```java
public int singleNumber(int[] nums) {
    int result = 0;
    for (int num : nums) {
        result ^= num;
    }
    return result;
}
```

**题目5**: 找出数组中两个出现一次的数字
```java
public int[] singleNumberIII(int[] nums) {
    int xor = 0;
    for (int num : nums) {
        xor ^= num;
    }
    
    // 找到最右边的1
    int rightmost = xor & -xor;
    
    int num1 = 0, num2 = 0;
    for (int num : nums) {
        if ((num & rightmost) == 0) {
            num1 ^= num;
        } else {
            num2 ^= num;
        }
    }
    return new int[]{num1, num2};
}
```

## 二、中等难度练习题

### 2.1 前缀树应用
**题目6**: 实现最大异或对查找
```java
class TrieNode {
    TrieNode[] children = new TrieNode[2];
}

class Solution {
    public int findMaximumXOR(int[] nums) {
        TrieNode root = new TrieNode();
        int max = 0;
        
        for (int num : nums) {
            TrieNode node = root;
            TrieNode xorNode = root;
            int currentMax = 0;
            
            for (int i = 31; i >= 0; i--) {
                int bit = (num >> i) & 1;
                int desiredBit = 1 - bit;
                
                if (node.children[bit] == null) {
                    node.children[bit] = new TrieNode();
                }
                node = node.children[bit];
                
                if (xorNode.children[desiredBit] != null) {
                    currentMax += (1 << i);
                    xorNode = xorNode.children[desiredBit];
                } else {
                    xorNode = xorNode.children[bit];
                }
            }
            max = Math.max(max, currentMax);
        }
        return max;
    }
}
```

### 2.2 区间查询问题
**题目7**: 子数组异或查询
```java
public int[] xorQueries(int[] arr, int[][] queries) {
    int n = arr.length;
    int[] prefix = new int[n + 1];
    
    for (int i = 1; i <= n; i++) {
        prefix[i] = prefix[i - 1] ^ arr[i - 1];
    }
    
    int[] result = new int[queries.length];
    for (int i = 0; i < queries.length; i++) {
        int l = queries[i][0], r = queries[i][1];
        result[i] = prefix[r + 1] ^ prefix[l];
    }
    return result;
}
```

## 三、高级练习题

### 3.1 树上的异或问题
**题目8**: 树上最长异或路径
```java
class Solution {
    class TrieNode {
        TrieNode[] children = new TrieNode[2];
    }
    
    public int xorLongestPath(int n, int[][] edges) {
        // 构建邻接表
        List<int[]>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            graph[u].add(new int[]{v, w});
            graph[v].add(new int[]{u, w});
        }
        
        int[] xorPath = new int[n];
        boolean[] visited = new boolean[n];
        dfs(0, -1, 0, graph, xorPath, visited);
        
        TrieNode root = new TrieNode();
        int maxXOR = 0;
        
        for (int path : xorPath) {
            insert(root, path);
            maxXOR = Math.max(maxXOR, query(root, path));
        }
        
        return maxXOR;
    }
    
    private void dfs(int node, int parent, int currentXOR, 
                    List<int[]>[] graph, int[] xorPath, boolean[] visited) {
        visited[node] = true;
        xorPath[node] = currentXOR;
        
        for (int[] neighbor : graph[node]) {
            int nextNode = neighbor[0];
            int weight = neighbor[1];
            if (nextNode != parent && !visited[nextNode]) {
                dfs(nextNode, node, currentXOR ^ weight, graph, xorPath, visited);
            }
        }
    }
    
    private void insert(TrieNode root, int num) {
        TrieNode node = root;
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            if (node.children[bit] == null) {
                node.children[bit] = new TrieNode();
            }
            node = node.children[bit];
        }
    }
    
    private int query(TrieNode root, int num) {
        TrieNode node = root;
        int maxXOR = 0;
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            int desiredBit = 1 - bit;
            if (node.children[desiredBit] != null) {
                maxXOR |= (1 << i);
                node = node.children[desiredBit];
            } else {
                node = node.children[bit];
            }
        }
        return maxXOR;
    }
}
```

### 3.2 莫队算法应用
**题目9**: 统计异或值等于k的子数组个数
```java
public int[] countXorSubarrays(int[] nums, int k, int[][] queries) {
    int n = nums.length;
    int q = queries.length;
    
    int[] prefix = new int[n + 1];
    for (int i = 1; i <= n; i++) {
        prefix[i] = prefix[i - 1] ^ nums[i - 1];
    }
    
    // 莫队算法实现
    int blockSize = (int) Math.sqrt(n);
    int[][] indexedQueries = new int[q][3];
    for (int i = 0; i < q; i++) {
        indexedQueries[i][0] = queries[i][0];
        indexedQueries[i][1] = queries[i][1];
        indexedQueries[i][2] = i;
    }
    
    Arrays.sort(indexedQueries, (a, b) -> {
        int blockA = a[0] / blockSize;
        int blockB = b[0] / blockSize;
        if (blockA != blockB) return Integer.compare(blockA, blockB);
        return Integer.compare(a[1], b[1]);
    });
    
    int[] result = new int[q];
    Map<Integer, Integer> freq = new HashMap<>();
    int l = 0, r = -1;
    long count = 0;
    
    freq.put(0, 1);
    
    for (int[] query : indexedQueries) {
        int left = query[0], right = query[1], idx = query[2];
        
        while (l < left) {
            int xor = prefix[l];
            freq.put(xor, freq.get(xor) - 1);
            count -= freq.getOrDefault(xor ^ k, 0);
            l++;
        }
        
        while (l > left) {
            l--;
            int xor = prefix[l];
            count += freq.getOrDefault(xor ^ k, 0);
            freq.put(xor, freq.getOrDefault(xor, 0) + 1);
        }
        
        while (r < right) {
            r++;
            int xor = prefix[r + 1];
            count += freq.getOrDefault(xor ^ k, 0);
            freq.put(xor, freq.getOrDefault(xor, 0) + 1);
        }
        
        while (r > right) {
            int xor = prefix[r + 1];
            freq.put(xor, freq.get(xor) - 1);
            count -= freq.getOrDefault(xor ^ k, 0);
            r--;
        }
        
        result[idx] = (int) count;
    }
    
    return result;
}
```

## 四、竞赛级别题目

### 4.1 Codeforces经典题目
**题目10**: Little Girl and Maximum XOR (Codeforces 276D)
```java
public long maximumXOR(long l, long r) {
    if (l == r) return 0;
    long xor = l ^ r;
    long highestBit = Long.highestOneBit(xor);
    return (highestBit << 1) - 1;
}
```

**题目11**: XOR and Favorite Number (Codeforces 617E)
```java
// 使用莫队算法实现，见题目9
```

### 4.2 LeetCode难题
**题目12**: 最大异或值(LeetCode 421)
```java
// 使用前缀树实现，见题目6
```

**题目13**: 统计异或值在范围内的数对(LeetCode 1803)
```java
class Solution {
    class TrieNode {
        TrieNode[] children = new TrieNode[2];
        int count = 0;
    }
    
    public int countPairs(int[] nums, int low, int high) {
        TrieNode root = new TrieNode();
        int result = 0;
        
        for (int num : nums) {
            result += query(root, num, high) - query(root, num, low - 1);
            insert(root, num);
        }
        return result;
    }
    
    private void insert(TrieNode root, int num) {
        TrieNode node = root;
        for (int i = 14; i >= 0; i--) {
            int bit = (num >> i) & 1;
            if (node.children[bit] == null) {
                node.children[bit] = new TrieNode();
            }
            node = node.children[bit];
            node.count++;
        }
    }
    
    private int query(TrieNode root, int num, int limit) {
        TrieNode node = root;
        int count = 0;
        
        for (int i = 14; i >= 0; i--) {
            int bitNum = (num >> i) & 1;
            int bitLimit = (limit >> i) & 1;
            
            if (bitLimit == 1) {
                if (node.children[bitNum] != null) {
                    count += node.children[bitNum].count;
                }
                if (node.children[1 - bitNum] != null) {
                    node = node.children[1 - bitNum];
                } else {
                    return count;
                }
            } else {
                if (node.children[bitNum] != null) {
                    node = node.children[bitNum];
                } else {
                    return count;
                }
            }
        }
        count += node.count;
        return count;
    }
}
```

## 五、实战训练建议

### 5.1 训练计划
1. **第一周**: 完成基础练习题(1-5题)
2. **第二周**: 完成中等难度题(6-9题)  
3. **第三周**: 完成高级练习题(10-13题)
4. **第四周**: 综合训练和竞赛模拟

### 5.2 测试用例设计
为每个题目设计全面的测试用例：
- 正常情况测试
- 边界条件测试
- 极端数据测试
- 性能压力测试

### 5.3 性能优化练习
尝试对每个题目进行性能优化：
- 减少时间复杂度
- 优化空间复杂度
- 提高代码可读性
- 添加错误处理

通过系统完成这些练习题，可以全面掌握异或运算在各种场景下的应用技巧。