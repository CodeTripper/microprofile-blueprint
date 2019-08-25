#!/bin/bash
#
# Script to initialise project by executing steps as follows:
#   - Replace package `in.codetripper`
#   - Replace `blueprint` to project Name
#   - Clean-up README file from template related info
#   - Self-destruct

read -p "Replace \`in.codetripper\` package name (in lower case) with : " package
read -p "Replace \`blueprint\` project name (in lower case) with: " project
pushd $(dirname "$0")/.. > /dev/null

IFS='.' read -ra package_array <<< "$package"

echo "creating package ${package_array[0]}.${package_array[1]} "
echo "creating project $project"


# Replace package name from non java files
echo "replacing project artifacts with the package name $package"
declare -a files_with_package=(pom.xml)
for i in ${files_with_package[@]}
do
  perl -i -pe "s/in.codetripper/$package/g" ${i}
done


# Replace imports from java files
find ./src -type f -print0 | xargs -0 perl -i -pe "s/in.codetripper.blueprint/$package.$project/g"

# move java files to correct package
echo "moving java source files to $package"
mkdir -p src/main/java/${package_array[0]}/${package_array[1]}
if [ $project == "blueprint" ]
then
	mv src/main/java/in/codetripper/blueprint src/main/java/${package_array[0]}/${package_array[1]}/${project}
else
	# Replace blueprint project from non java files
	declare -a files_with_blueprint=(pom.xml Dockerfile README.md)
	echo "replacing project artifacts with the project name $project"
	for i in ${files_with_blueprint[@]}
	do
	  perl -i -pe "s/blueprint/$project/g" ${i}
	done
	# move java files to correct package
	mv src/main/java/in/codetripper/blueprint src/main/java/${package_array[0]}/${package_array[1]}
	#echo "rename application file to $project Application"
	#mv src/main/java/${package_array[0]}/${package_array[1]}/${project}/BlueprintApplication.java src/main/java/${package_array[0]}/${package_array[1]}/${project}/${project^}Application.java
	#perl -i -pe "s/BlueprintApplication/${project^}Application/" src/main/java/${package_array[0]}/${package_array[1]}/${project}/${project^}Application.java
	#perl -i -pe "s/.*\n/# $project\n/g if 1 .. 1" README.md
fi

# Self-destruct
rootPackage="${package_array[0]}"
if [ $rootPackage == "in" ]
then
	echo "com package - deleting codetripper"
	rm -rf src/main/java/in/codetripper/
else
	echo "non com package - deleting in"
	rm -rf src/main/java/in/
fi

#rm bin/init.sh

git init
git add --all
git commit -a -m "Project Created"

# Return to original directory
popd > /dev/null

