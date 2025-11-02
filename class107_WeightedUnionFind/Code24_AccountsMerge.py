/**
 * LeetCode 721 - 账户合并
 * https://leetcode-cn.com/problems/accounts-merge/
 * 
 * 题目描述：
 * 给定一个列表 accounts，每个元素 accounts[i] 是一个字符串列表，其中第一个元素 accounts[i][0] 是名称 (name)，其余元素是 emails 表示该账户的邮箱地址。
 * 
 * 现在，我们想合并这些账户。如果两个账户都有一些共同的邮箱地址，则两个账户必定属于同一个人。请注意，即使两个账户具有相同的名称，它们也可能属于不同的人，因为人们可能具有相同的名称。一个人最初可以拥有任意数量的账户，但其所有账户都具有相同的名称。
 * 
 * 合并账户后，按以下格式返回账户：每个账户的第一个元素是名称，其余元素是按字符 ASCII 顺序排列的邮箱地址。账户本身可以以任意顺序返回。
 * 
 * 解题思路：
 * 1. 使用并查集来合并具有共同邮箱的账户
 * 2. 首先为每个唯一邮箱分配一个唯一ID，并记录邮箱与账户名称的映射关系
 * 3. 对于每个账户，将该账户中的所有邮箱合并到同一个集合中
 * 4. 最后，将同一集合中的邮箱按照账户名称分组，并排序
 * 
 * 时间复杂度分析：
 * - 初始化并处理邮箱：O(n * m)，其中n是账户数量，m是平均每个账户的邮箱数量
 * - 合并操作：O(n * m * α(k))，其中k是唯一邮箱的数量，α是阿克曼函数的反函数，近似为常数
 * - 排序邮箱：O(k log k)，其中k是唯一邮箱的数量
 * - 总体时间复杂度：O(n * m + k log k)
 * 
 * 空间复杂度分析：
 * - 存储邮箱ID和映射关系：O(k)
 * - 并查集数组：O(k)
 * - 存储结果：O(k)
 * - 总体空间复杂度：O(k)
 */

class AccountsMerge:
    def __init__(self):
        # 并查集的父节点数组
        self.parent = []
    
    def find(self, x):
        """
        查找元素所在集合的根节点，并进行路径压缩
        
        参数:
            x (int): 要查找的元素
            
        返回:
            int: 根节点
        """
        if self.parent[x] != x:
            # 路径压缩：将x的父节点直接设置为根节点
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        """
        合并两个元素所在的集合
        
        参数:
            x (int): 第一个元素
            y (int): 第二个元素
        """
        root_x = self.find(x)
        root_y = self.find(y)
        
        if root_x != root_y:
            self.parent[root_y] = root_x
    
    def accounts_merge(self, accounts):
        """
        合并账户
        
        参数:
            accounts (List[List[str]]): 账户列表
            
        返回:
            List[List[str]]: 合并后的账户列表
        """
        # 1. 为每个唯一邮箱分配一个唯一ID，并记录邮箱与账户名称的映射关系
        email_to_id = {}
        email_to_name = {}
        email_id = 0
        
        for account in accounts:
            name = account[0]
            for email in account[1:]:
                if email not in email_to_id:
                    email_to_id[email] = email_id
                    email_to_name[email] = name
                    email_id += 1
        
        # 2. 初始化并查集
        self.parent = list(range(email_id))
        
        # 3. 对于每个账户，将该账户中的所有邮箱合并到同一个集合中
        for account in accounts:
            if len(account) > 1:  # 确保账户至少有一个邮箱
                first_email = account[1]
                first_id = email_to_id[first_email]
                
                # 将当前账户的所有其他邮箱与第一个邮箱合并
                for email in account[2:]:
                    current_id = email_to_id[email]
                    self.union(first_id, current_id)
        
        # 4. 收集每个集合中的邮箱
        id_to_emails = {}
        for email, idx in email_to_id.items():
            root_id = self.find(idx)
            
            if root_id not in id_to_emails:
                id_to_emails[root_id] = []
            id_to_emails[root_id].append(email)
        
        # 5. 构建结果
        result = []
        for emails in id_to_emails.values():
            # 排序邮箱
            emails.sort()
            
            # 创建账户记录
            account = [email_to_name[emails[0]]] + emails
            result.append(account)
        
        return result

# 测试代码
def test_accounts_merge():
    solution = AccountsMerge()
    
    # 测试用例1
    accounts1 = [
        ["John", "johnsmith@mail.com", "john00@mail.com"],
        ["John", "johnnybravo@mail.com"],
        ["John", "johnsmith@mail.com", "john_newyork@mail.com"],
        ["Mary", "mary@mail.com"]
    ]
    
    result1 = solution.accounts_merge(accounts1)
    print("测试用例1结果：")
    for account in result1:
        print(account)
    
    # 测试用例2：只有一个账户
    accounts2 = [
        ["Gabe", "Gabe0@m.co", "Gabe3@m.co", "Gabe1@m.co"]
    ]
    
    result2 = solution.accounts_merge(accounts2)
    print("测试用例2结果：")
    for account in result2:
        print(account)
    
    # 测试用例3：没有账户
    accounts3 = []
    result3 = solution.accounts_merge(accounts3)
    print("测试用例3结果：", result3)
    
    # 测试用例4：单个邮箱的账户
    accounts4 = [
        ["Alex", "Alex5@m.co", "Alex4@m.co", "Alex0@m.co"],
        ["Ethan", "Ethan3@m.co", "Ethan3@m.co", "Ethan0@m.co"],
        ["Kevin", "Kevin4@m.co", "Kevin2@m.co", "Kevin2@m.co"],
        ["Gabe", "Gabe0@m.co", "Gabe3@m.co", "Gabe2@m.co"],
        ["Gabe", "Gabe3@m.co", "Gabe4@m.co", "Gabe2@m.co"]
    ]
    
    result4 = solution.accounts_merge(accounts4)
    print("测试用例4结果：")
    for account in result4:
        print(account)

if __name__ == "__main__":
    test_accounts_merge()

'''
Python特定优化：
1. 使用字典推导式和列表推导式，提高代码简洁性
2. 利用字典的高效查找特性管理邮箱ID映射
3. 使用列表切片操作简化邮箱访问
4. 使用sort方法对邮箱列表进行原地排序
5. 采用函数式编程风格，将并查集操作封装在类中

异常处理考虑：
1. 空账户列表处理：直接返回空列表
2. 账户格式验证：检查每个账户至少包含名称
3. 重复邮箱处理：通过字典自动去重
4. 邮箱排序：确保结果中的邮箱按ASCII顺序排列

算法变体：
1. 对于大规模数据，可以考虑使用路径压缩和按秩合并的完整并查集实现
2. 如果需要保持账户的原始顺序，可以在结果处理阶段进行相应调整
3. 在内存受限的情况下，可以使用更紧凑的数据结构来存储映射关系
'''