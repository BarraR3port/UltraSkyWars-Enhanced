/*
 * Copyright (c) 2022.
 *
 *  This program/library is free software: you can redistribute it and/or modify
 * it under the terms of the New BSD License (3-clause license).
 *
 * This program/library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the New BSD License (3-clause license)
 * for more details.
 *
 * You should have received a copy of the New BSD License (3-clause license)
 * along with this program/library; If not, see http://directory.fsf.org/wiki/License:BSD_3Clause/
 * for the New BSD License (3-clause license).
 *
 */

package io.github.Leonardo0013YT.UltraSkyWars.api.modules.parties.party;

import java.util.UUID;

public class PartyInvite {
    
    private final UUID inviter;
    private final UUID invited;
    
    public PartyInvite(UUID inviter, UUID invited){
        this.inviter = inviter;
        this.invited = invited;
    }
    
    public UUID getInviter(){
        return inviter;
    }
    
    public UUID getInvited(){
        return invited;
    }
}