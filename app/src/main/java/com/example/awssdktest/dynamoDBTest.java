package com.example.awssdktest;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;

@DynamoDBTable(tableName = "aws_sdk_test")
public class dynamoDBTest {

    private int id;
    private int price;
    /* パーティションキーで指定した属性名 */
    @DynamoDBHashKey(attributeName = "id")
    public int getID() {return id; }
    public void setID(int id) { this.id = id; }

    /* 任意の属性名 AWSコンソールで事前定義不要*/
    @DynamoDBAttribute(attributeName = "Price")
    public int getPrice() {return price; }
    public void setPrice(int price) { this.price = price; }

}
