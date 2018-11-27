ARCH="index.zip"
FUNC_NAME="AlexisLambda"

if npm install ; then
	rm $ARCH
	zip -r $ARCH .
	aws lambda update-function-code --profile alexisadmin --function-name $FUNC_NAME --zip-file fileb://$ARCH
else
	echo ''
fi
