/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mskcc.cbio.oncokb.bo.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.mskcc.cbio.oncokb.bo.AlterationBo;
import org.mskcc.cbio.oncokb.bo.EvidenceBo;
import org.mskcc.cbio.oncokb.bo.GeneBo;
import org.mskcc.cbio.oncokb.dao.AlterationDao;
import org.mskcc.cbio.oncokb.model.Alteration;
import org.mskcc.cbio.oncokb.model.AlterationType;
import org.mskcc.cbio.oncokb.model.Evidence;
import org.mskcc.cbio.oncokb.model.EvidenceType;
import org.mskcc.cbio.oncokb.model.Gene;
import org.mskcc.cbio.oncokb.model.VariantConsequence;
import org.mskcc.cbio.oncokb.util.ApplicationContextSingleton;

/**
 *
 * @author jgao
 */
public class AlterationBoImpl extends GenericBoImpl<Alteration, AlterationDao> implements AlterationBo {

    @Override
    public List<Alteration> findAlterationsByGene(Collection<Gene> genes) {
        List<Alteration> alterations = new ArrayList<Alteration>();
        for (Gene gene : genes) {
            alterations.addAll(getDao().findAlterationsByGene(gene));
        }
        return alterations;
    }
    
    @Override
    public Alteration findAlteration(Gene gene, AlterationType alterationType, String alteration) {
        return getDao().findAlteration(gene, alterationType, alteration);
    }

    @Override
    public List<Alteration> findMutationsByConsequenceAndPosition(Gene gene, VariantConsequence consequence, int start, int end) {
        return getDao().findMutationsByConsequenceAndPosition(gene, consequence, start, end);
    }

    @Override
    public List<Alteration> findRelevantAlterations(Alteration alteration) {
        List<Alteration> alterations = new ArrayList<Alteration>();
        Alteration matchedAlt = findAlteration(alteration.getGene(), alteration.getAlterationType(), alteration.getAlteration());
        if (matchedAlt!=null) {
            alterations.add(matchedAlt);
        }
        if (alteration.getConsequence()!=null) {
            // we need to develop better way to match mutation
            if (alteration.getProteinStart()!=null) {
                List<Alteration> alts = findMutationsByConsequenceAndPosition(alteration.getGene(), alteration.getConsequence(), alteration.getProteinStart(), alteration.getProteinEnd());
                if (!alteration.getConsequence().getTerm().equals("missense_variant")) {
                    alterations.addAll(alts);
                } else {
                    for (Alteration alt : alts) {
                        if (!alt.getAlteration().matches("[A-Z][0-9]+[A-Z]")) {
                            alterations.add(alt);
                        }
                    }
                }
            }

            if (alteration.getConsequence().getIsGenerallyTruncating()) {
                VariantConsequence truncatingVariantConsequence = ApplicationContextSingleton.getVariantConsequenceBo().findVariantConsequenceByTerm("feature_truncation");
                alterations.addAll(findMutationsByConsequenceAndPosition(alteration.getGene(), truncatingVariantConsequence, alteration.getProteinStart(), alteration.getProteinEnd()));
            }
        }

        VariantConsequence anyConsequence = ApplicationContextSingleton.getVariantConsequenceBo().findVariantConsequenceByTerm("any");
        alterations.addAll(findMutationsByConsequenceAndPosition(alteration.getGene(), anyConsequence, alteration.getProteinStart(), alteration.getProteinEnd()));
            
        //TODO: add activating or inactivating alterations
        EvidenceBo evidenceBo = ApplicationContextSingleton.getEvidenceBo();
        List<Evidence> mutationEffectEvs = evidenceBo.findEvidencesByAlteration(alterations, Collections.singleton(EvidenceType.MUTATION_EFFECT));
        boolean activating = false, inactivating = false;
        for (Evidence evidence : mutationEffectEvs) {
            String effect = evidence.getKnownEffect();
            if(effect != null) {
                effect = effect.toLowerCase();
                if (effect.contains("inactivating")) {
                    inactivating = true;
                } else if (effect.contains("activating")) {
                    activating = true;
                }
            }
        }

        //If alteration contains 'fusion'
        if(alteration.getAlteration().toLowerCase().contains("fusion")) {
            String fusion = alteration.getAlteration(); // e.g. TRB-NKX2-1 fusion
            int ix = fusion.toLowerCase().indexOf("fusion");
            if (ix>0) {
                // find fusions annotated in the other gene
                String gene = alteration.getGene().getHugoSymbol();
                String genes = fusion.substring(0,ix);
                int ixg = genes.indexOf(gene);
                if (ixg<0) {
                    System.err.println(fusion + " was under " + gene);
                } else {
                    String theOtherGene = genes.replace(gene, "")
                            .replaceAll("-", " ").trim() // trim -
                            .replaceAll(" ", "-");
                    
                    GeneBo geneBo = ApplicationContextSingleton.getGeneBo();
                    Gene tog = geneBo.findGeneByHugoSymbol(theOtherGene);
                    if (tog!=null) {
                        String reverse;
                        if (ixg==0) {
                            reverse = tog.getHugoSymbol()+"-"+gene+" fusion";
                        } else {
                            reverse = gene+"-"+tog.getHugoSymbol()+" fusion";
                        }
                        
                        Alteration toa = findAlteration(tog, alteration.getAlterationType(), reverse);
                        if (toa!=null) {
                            alterations.add(toa);
                        }
                        
                        toa = findAlteration(tog, alteration.getAlterationType(), "fusions");
                        if (toa!=null) {
                            alterations.add(toa);
                        }
                    }
                }
                
            }
            
            //the alteration 'fusions' should be injected into alteration list
            Alteration alt = findAlteration(alteration.getGene(), alteration.getAlterationType(), "fusions");
            if (alt!=null) {
                alterations.add(alt);
            }
        }
        
        if (inactivating) {
            Alteration alt = findAlteration(alteration.getGene(), alteration.getAlterationType(), "inactivating mutations");
            if (alt!=null) {
                alterations.add(alt);
            }
        }
        
        if (activating) {
            Alteration alt = findAlteration(alteration.getGene(), alteration.getAlterationType(), "activating mutations");
            if (alt!=null) {
                alterations.add(alt);
            }
        }
        
        return alterations;
    }
}
