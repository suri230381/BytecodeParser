/*
 *  Copyright (C) 2011 Stephane Godbillon
 *  
 *  This file is part of BytecodeParser. See the README file in the root
 *  directory of this project.
 *
 *  BytecodeParser is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  BytecodeParser is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.

 *  You should have received a copy of the GNU Lesser General Public License
 *  along with BytecodeParser.  If not, see <http://www.gnu.org/licenses/>.
 */
package bytecodeparser.analysis.decoders;

import java.util.Arrays;

import bytecodeparser.Context;
import bytecodeparser.analysis.opcodes.ArrayOpcode;
import bytecodeparser.analysis.stack.Constant.IntegerConstant;
import bytecodeparser.analysis.stack.Stack;
import bytecodeparser.analysis.stack.Stack.StackElementLength;
import bytecodeparser.analysis.stack.StackElement;
import bytecodeparser.analysis.stack.TrackableArray;

public class DecodedArrayOp extends DecodedBasicOp {
	public DecodedArrayOp(ArrayOpcode op, Context context, int index) {
		super(op, context, index);
	}
	@Override
	public void simulate(Stack stack) {
		if(!this.op.as(ArrayOpcode.class).isLoad) {
			StackElementLength[] pops = Arrays.copyOf(getPops(), getPops().length - 1);
			StackElement subject = stack.getFromTop(StackElementLength.add(pops));
			//System.out.println("Array: subject is " + subject);
			if(subject instanceof TrackableArray) {
				TrackableArray array = (TrackableArray) subject;
				//System.out.println("subject is a trackable array ! get from top " + StackElementLength.add(array.componentLength));
				StackElement i = stack.getFromTop(StackElementLength.add(array.componentLength));
				if(i instanceof IntegerConstant) {
					StackElement value = stack.peek(array.componentLength);
					//System.out.println("ok, put value "+value+" at index " + i);
					array.set(((IntegerConstant) i).getValue(), value);
				} else {
					//System.out.println("NOK!!, index " + i + " is not IntegerConstant!");
					array.isDirty = true;
				}
			}
		}
		super.simulate(stack);
	}
}
