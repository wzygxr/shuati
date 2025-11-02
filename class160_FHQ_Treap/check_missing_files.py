import os
import re

def check_missing_implementations():
    directory = "."
    
    # 获取所有代码文件
    files = os.listdir(directory)
    
    # 按代码编号分组
    code_groups = {}
    
    for file in files:
        if file.startswith("Code") and (file.endswith(".java") or file.endswith(".cpp") or file.endswith(".py")):
            # 提取代码编号（如Code01_FHQTreapWithCount1）
            match = re.match(r'(Code\d+_[^.]*)', file)
            if match:
                code_name = match.group(1)
                if code_name not in code_groups:
                    code_groups[code_name] = set()
                code_groups[code_name].add(file.split('.')[-1])  # 文件扩展名
    
    # 检查每个代码组是否缺少语言实现
    missing_implementations = []
    
    for code_name, extensions in code_groups.items():
        if len(extensions) < 3:
            missing_langs = []
            if 'java' not in extensions:
                missing_langs.append('Java')
            if 'cpp' not in extensions:
                missing_langs.append('C++')
            if 'py' not in extensions:
                missing_langs.append('Python')
            
            missing_implementations.append({
                'code_name': code_name,
                'existing': list(extensions),
                'missing': missing_langs
            })
    
    return missing_implementations

if __name__ == "__main__":
    missing = check_missing_implementations()
    
    if missing:
        print("需要补充语言实现的文件：")
        print("=" * 60)
        
        for item in missing:
            print(f"代码: {item['code_name']}")
            print(f"  已有: {', '.join(item['existing'])}")
            print(f"  缺失: {', '.join(item['missing'])}")
            print()
    else:
        print("所有代码文件都已包含Java、C++和Python三种语言的实现！")
        print("=" * 60)