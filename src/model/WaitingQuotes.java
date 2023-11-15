/*
 * Copyright (c) 2020, Bear Au Jus - ジュースとくま
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Bear Au Jus - ジュースとくま
 */
public class WaitingQuotes {

    private static final List<String> WAITINGMODEL = new ArrayList<>(
            Arrays.asList(
                    "All Set! Please wait a moment . . .",
                    "All Green! Please wait a moment . . .",
                    "Almost there friend! Please wait for a moment . . .",
                    "Ready for battle? Zombie Pigman is waiting for you :3",
                    "Cutting the edge rightnow, Please wait for a moment . . .",
                    "Our system is preparing, please wait for a moment . . .",
                    "Beep Beep, we are in the yellow status, please wait for a moment . . .",
                    "Using ninjutsu rightnow! please wait for a moment . . .",
                    "Did you know the Pillager came from Villager ?",
                    "Iron Golem try hard to protecting the Villagers !",
                    "Baby Villager or Baby Pillager ?",
                    "Enchanting diamond pickaxe rightnow !",
                    "Ender Dragon is waiting for you !"));

    public static String getRandomWaiting() {
        return WAITINGMODEL.get(((int) (Math.random() * WAITINGMODEL.size())));
    }
}
