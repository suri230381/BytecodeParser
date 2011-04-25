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

import static bytecodeparser.analysis.stack.Stack.StackElementLength.DOUBLE;
import bytecodeparser.Context;
import bytecodeparser.analysis.LocalVariable;
import bytecodeparser.analysis.opcodes.LocalVariableOpcode;
import bytecodeparser.analysis.stack.Stack;
import bytecodeparser.analysis.stack.ValueFromLocalVariable;

public class DecodedLocalVariableOp extends DecodedBasicOp {
	public final LocalVariable localVariable;
	public final boolean load;
	
	public DecodedLocalVariableOp(LocalVariableOpcode lvo, Context context, int index) {
		super(lvo, context, index);
		int slot;
		if(parameterTypes.length > 0)
			slot = parameterValues[0];
		else slot = lvo.getCode() - lvo.getBaseOpcode();
		this.load = lvo.isLoad();
		localVariable = LocalVariable.getLocalVariable(slot, index, context.localVariables);
	}
	
	@Override
	public void simulate(Stack stack) {
		ValueFromLocalVariable toPush = new ValueFromLocalVariable(localVariable);
		//System.out.println("load " + index + ":" + getName() + " " + toPush.localVariable + "? " + load);
		for(int i = 0; i < getPops().length; i++) {
			//System.out.println("pop");
			if(getPops()[i] == DOUBLE)
				stack.pop2();
			else stack.pop();
		}
		for(int i = 0; i < getPushes().length; i++) {
			//System.out.println("push");
			if(getPushes()[i] == DOUBLE)
				stack.push2(toPush);
			else stack.push(toPush);
		}
	}
}