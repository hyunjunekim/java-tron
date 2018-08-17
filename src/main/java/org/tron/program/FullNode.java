package org.tron.program;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.tron.common.application.Application;
import org.tron.common.application.ApplicationFactory;
import org.tron.common.runtime.vm.VM;
import org.tron.common.runtime.vm.program.Program;
import org.tron.common.runtime.vm.program.invoke.ProgramInvokeFactory;
import org.tron.common.runtime.vm.program.invoke.ProgramInvokeFactoryImpl;
import org.tron.common.runtime.vm.program.invoke.ProgramInvokeImpl;
import org.tron.core.Constant;
import org.tron.core.config.DefaultConfig;
import org.tron.core.config.args.Args;
import org.tron.core.db.RevokingDatabase;
import org.tron.core.exception.ContractValidateException;
import org.tron.core.services.RpcApiService;
import org.tron.core.services.WitnessService;
import org.tron.core.services.http.FullNodeHttpApiService;

@Slf4j
public class FullNode {

  /**
   * Start the FullNode.
   */
  public static void main(String[] args) throws InterruptedException {
    // This code is just POC for TVM
    VM vm = new VM();
    ProgramInvokeImpl mockInvoke = new ProgramInvokeImpl();
    // This is byte codes about the smart contrack
    String s ="5b600056";
    // 0x5b      - JUMPTEST
    // 0x60 0x00 - PUSH 0x00
    // 0x56      - JUMP to 0

    int len = s.length();
    byte[] op = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      op[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                         + Character.digit(s.charAt(i+1), 16));
    }

    Program program = new Program(op, mockInvoke);
      // The infinite Loop
      // Like while(1);
    try {
      vm.play(program);
    } catch (ContractValidateException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void shutdown(final Application app) {
    logger.info("********register application shutdown hook********");
    Runtime.getRuntime().addShutdownHook(new Thread(app::shutdown));
  }
}
