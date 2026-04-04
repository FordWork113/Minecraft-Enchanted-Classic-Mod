package com.mojang.minecraft.model;

import com.mojang.minecraft.model.Model;
import com.mojang.minecraft.model.ModelPart;
import com.mojang.util.MathHelper;

public class ChickenModel extends Model {

	public ModelPart head;
	public ModelPart body;
	public ModelPart rightLeg;
	public ModelPart leftLeg;
	public ModelPart rightWing;
	public ModelPart leftWing;
	public ModelPart bill;
	public ModelPart chin;
	   
public ChickenModel() {
    this.head = new ModelPart(0, 0);
    this.head.setBounds(-2.0F, -6.0F, -2.0F, 4, 6, 3, 0.0F);
    this.head.setPosition(0.0F, 16.0F, -4.0F);
    this.bill = new ModelPart(14, 0);
    this.bill.setBounds(-2.0F, -4.0F, -4.0F, 4, 2, 2, 0.0F);
    this.bill.setPosition(0.0F, 16.0F, -4.0F);
    this.chin = new ModelPart(14, 4);
    this.chin.setBounds(-1.0F, -2.0F, -3.0F, 2, 2, 2, 0.0F);
    this.chin.setPosition(0.0F, 16.0F, -4.0F);
    this.body = new ModelPart(0, 9);
    this.body.setBounds(-3.0F, -4.0F, -3.0F, 6, 8, 6, 0.0F);
    this.body.setPosition(0.0F, 16.0F, 0.0F);
    this.rightLeg = new ModelPart(26, 0);
	this.rightLeg.setBounds(-1.0F, 3.0F, -3.0F, 3, 5, 3, 0.0F);
	this.rightLeg.setPosition(-2.0F, 16.0F, 1.0F);
	this.leftLeg = new ModelPart(26, 0);
	this.leftLeg.setBounds(-1.0F, 3.0F, -3.0F, 3, 5, 3, 0.0F);
	this.leftLeg.setPosition(1.0F, 16.0F, 1.0F);
	this.rightWing = new ModelPart(24, 13);
	this.rightWing.setBounds(0.0F, -3.0F, -3.0F, 1, 4, 6, 0.0F);
	this.rightWing.setPosition(-4.0F, 16.0F, 0.0F);
	this.leftWing = new ModelPart(24, 13);
	this.leftWing.setBounds(-1.0F, -3.0F, -3.0F, 1, 4, 6, 0.0F);
	this.leftWing.setPosition(4.0F, 16.0F, 0.0F);
}

public final void render(float var1, float var2, float var3, float var4, float var5, float var6) {
    this.head.render(var6);
    this.body.render(var6);
    this.body.pitch = 1.5707964F;
    this.bill.render(var6);
    this.bill.render(var6);
    this.rightLeg.render(var6);
    this.leftLeg.render(var6);
    this.leftLeg.pitch = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2;
    this.rightLeg.pitch = MathHelper.cos(var1 * 0.6662F + 3.1415927F) * 1.4F * var2;
    this.rightWing.render(var6);
    this.leftWing.render(var6);
 }
}
